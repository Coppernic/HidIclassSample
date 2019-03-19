package fr.coppernic.sample.hidiclass.home

interface HomePresenter {
    fun setUp(view: HomeView)
    fun read()
    fun stop()
    fun dispose()
}