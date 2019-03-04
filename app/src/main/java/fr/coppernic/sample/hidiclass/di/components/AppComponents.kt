package fr.coppernic.sample.hidiclass.di.components

import dagger.Component
import fr.coppernic.sample.hidiclass.di.modules.ContextModule
import fr.coppernic.sample.hidiclass.home.HomeActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [(ContextModule::class)])
interface AppComponents {

    fun inject(homeActivity: HomeActivity)
}
