package fr.coppernic.sample.hidiclass.di.modules

import dagger.Binds
import dagger.Module
import fr.coppernic.sample.hidiclass.home.HomePresenter
import fr.coppernic.sample.hidiclass.home.HomePresenterImpl

@Module
interface HomePresenterModule{
    @Binds
    fun bindHomePresenter(presenterImpl: HomePresenterImpl): HomePresenter
}