package com.jkaszczynski.quoteoftheday.services.daos

interface Dao<E : Any, T> {

    fun get(id: T): E

    fun save(entity: E)

    fun remove(id: T)

    fun update(entity: E)
}