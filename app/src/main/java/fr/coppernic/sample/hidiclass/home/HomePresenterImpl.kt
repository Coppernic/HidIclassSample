package fr.coppernic.sample.hidiclass.home

import fr.coppernic.sample.hidiclass.interactor.HidIclassInteractor
import fr.coppernic.sample.hidiclass.settings.Settings
import fr.coppernic.sdk.hid.iclassProx.FrameProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class HomePresenterImpl @Inject constructor() : HomePresenter {

    lateinit var view: HomeView

    @Inject
    lateinit var hidIclassInteractor: HidIclassInteractor

    @Inject
    @field:Named("SETTINGS")
    lateinit var settings: Settings

    override fun setUp(view: HomeView) {
        this.view = view
        val disposable = hidIclassInteractor.power(true)
                .flatMapCompletable { hidIclassInteractor.setUp() }
                .andThen(hidIclassInteractor.open())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.showFab(true)
                }, {
                    view.showError(it.message!!)
                    view.showFab(false)
                })
    }

    override fun read() {

        if (settings.getProtocolList().isEmpty() && settings.isHfEnabled()) {
            view.showError("No Protocol Selected")
            return
        } else {
            val protocolList = settings.getProtocolList().map { FrameProtocol.valueOf(it) }
            hidIclassInteractor.disposables.add(hidIclassInteractor.continuousRead(protocolList.toTypedArray(), settings.isHfEnabled())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.cardSerialNumber != null) {
                            if (settings.isBeepEnabled()) {
                                view.playSound()
                            }
                            view.displayTags(it)
                        }
                    }, {
                        Timber.d(it.message!!)
                    }))
        }
    }

    override fun stop() {
        hidIclassInteractor.disposables.dispose()
    }

    override fun dispose() {
        hidIclassInteractor.dispose()
    }
}