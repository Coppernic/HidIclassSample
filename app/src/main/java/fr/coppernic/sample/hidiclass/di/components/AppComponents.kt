package fr.coppernic.sample.hidiclass.di.components

import dagger.Component
import fr.coppernic.sample.hidiclass.di.modules.AppModule
import fr.coppernic.sample.hidiclass.di.modules.ContextModule
import fr.coppernic.sample.hidiclass.di.modules.HomePresenterModule
import fr.coppernic.sample.hidiclass.home.HomeActivityDrawer
import javax.inject.Singleton

@Singleton
@Component(modules = [(ContextModule::class), HomePresenterModule::class, AppModule::class])
interface AppComponents {

    fun inject(homeActivity: HomeActivityDrawer)
}
