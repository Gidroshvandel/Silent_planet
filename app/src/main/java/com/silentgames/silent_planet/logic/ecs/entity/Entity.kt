package com.silentgames.silent_planet.logic.ecs.entity

import com.silentgames.silent_planet.logic.ecs.component.Component

open class Entity {

    val id = EntityIdGenerator.generateId()

    @PublishedApi
    internal val components: Set<Component>
        get() = localComponents

    private val localComponents: MutableSet<Component> = mutableSetOf()

    inline fun <reified T : Component> getComponent(): T? {
        return components.filterIsInstance<T>().firstOrNull()
    }

    inline fun <reified T : Component> hasComponent(): Boolean = getComponent<T>() != null

    fun addComponent(component: Component) {
        if (localComponents.contains(component)) {
            localComponents.remove(component)
        }
        localComponents.add(component)
    }

    fun removeComponent(component: Component) {
        localComponents.remove(component)
    }

    fun <T : Component> removeComponent(clazz: Class<T>) {
        localComponents.filterIsInstance(clazz).firstOrNull()?.let {
            removeComponent(it)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Entity) {
            other.id == id
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}