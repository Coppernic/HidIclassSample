package fr.coppernic.sample.hidiclass.interactor

import android.content.Context
import fr.coppernic.sdk.hid.iclassProx.*
import fr.coppernic.sdk.power.impl.cone.ConePeripheral
import fr.coppernic.sdk.utils.core.CpcDefinitions
import fr.coppernic.sdk.utils.core.CpcResult
import fr.coppernic.sdk.utils.io.InstanceListener
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HidIclassInteractor @Inject constructor() {

    @Inject
    lateinit var context: Context

    lateinit var reader: Reader

    var disposables = CompositeDisposable()
        get() {
            if (field.isDisposed) {
                // New composite disposable each time we subscribe
                field = CompositeDisposable()
            }
            return field
        }


    fun power(on: Boolean): Single<CpcResult.RESULT>{
        return ConePeripheral.RFID_HID_ICLASSPROX_GPIO.descriptor.power(context, on)
    }

    lateinit var completableEmitter: CompletableEmitter

    fun setUp(): Completable {
        return Completable.create {
                        Timber.d("get instance")
                        completableEmitter = it
                        Reader.getInstance(context, instanceListener)
                    }
    }

    fun open(): Completable {
        return Completable.create {
            var res = reader.open(CpcDefinitions.HID_ICLASS_PROX_READER_PORT, BaudRate.B9600)
            if(res != ErrorCodes.ER_OK){
                it.onError( Throwable(res.description))
            }else {
                res = reader.samCommandSuspendAutonomousMode()
                if (res != ErrorCodes.ER_OK) {
                    it.onError(Throwable(res.description))
                }else {
                    it.onComplete()
                }
            }
        }
    }

    fun read(frameProtocolList: Array<FrameProtocol>): Single<Card>{
        return Single.create{
            val card = Card()
            val res = reader.samCommandScanFieldForCard(frameProtocolList, card)
            if(res != ErrorCodes.ER_OK){
                Timber.d("error read")
                notifyError(it, Throwable(res.description))
            }else{
                notifySuccess(it, card)
            }

        }

    }

    fun readLF(): Single<Card>{
        return Single.create{
            val card = Card()
            val res = reader.samCommandScanAndProcessMedia(card)
            if(res != ErrorCodes.ER_OK){
                Timber.d("error read")
                notifyError(it, Throwable(res.description))
            }else{
                notifySuccess(it, card)
            }

        }

    }

    fun continuousRead(frameProtocolList: Array<FrameProtocol>, isHf: Boolean): Flowable<Card>{
        return Flowable.interval(100, TimeUnit.MILLISECONDS, Schedulers.single())
                .onBackpressureLatest()
                .flatMapSingle ({
                    if(!isHf){
                        return@flatMapSingle readLF().onErrorResumeNext(Single.just(Card()))
                    }else {
                        return@flatMapSingle read(frameProtocolList).onErrorResumeNext(Single.just(Card()))
                    }
                },false,1)
    }

    fun dispose(){
        disposables.dispose()
        reader.close()
    }

    private val instanceListener= object: InstanceListener<Reader>{
        override fun onDisposed(p0: Reader) {
        }

        override fun onCreated(hidReader: Reader) {
            Timber.d("hid reader instance created")
            reader = hidReader
            if(!completableEmitter.isDisposed) {
                completableEmitter.onComplete()
            }
        }
    }

    fun notifySuccess(emitter: SingleEmitter<Card>, card: Card) {
        if (!emitter.isDisposed) {
            emitter.onSuccess(card)
        }
    }

    fun notifyError(emitter: SingleEmitter<Card>, throwable: Throwable) {
        if (!emitter.isDisposed) {
            emitter.onError(throwable)
        }
    }

}