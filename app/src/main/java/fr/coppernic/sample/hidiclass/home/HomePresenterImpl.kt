package fr.coppernic.sample.hidiclass.home

import fr.coppernic.sample.hidiclass.interactor.HidIclassInteractor
import fr.coppernic.sdk.hid.iclassProx.FrameProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class HomePresenterImpl @Inject constructor() : HomePresenter{

    lateinit var view: HomeView

    @Inject
    lateinit var hidIclassInteractor: HidIclassInteractor

    override fun setUp(view: HomeView) {
        this.view = view
        val disposable = hidIclassInteractor.power(true)
                .flatMapCompletable{hidIclassInteractor.setUp()}
                .andThen(hidIclassInteractor.open())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.showFab(true)
                },{
                    view.showError(it.message!!)
                    view.showFab(false)
                })
    }

    override fun read() {
        hidIclassInteractor.disposables.add(hidIclassInteractor.continuousRead(FrameProtocol.values())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if(it.cardSerialNumber != null) {
                        view.displayTags(it)
                    }
                },{
                    Timber.d(it.message!!)
                }))
    }

    override fun stop() {
        hidIclassInteractor.disposables.dispose()
    }

    override fun dispose() {
        hidIclassInteractor.dispose()
    }
}