package com.example

import com.example.domain.Child
import com.example.domain.Parent
import com.example.domain.ParentRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ParentRepositoryTest {

    @Inject
    private lateinit var repository: ParentRepository

    @Test
    internal fun `save parent with children`() {
        val children = mutableListOf<Child>()
        val parent = Parent("parent", children)
        children.addAll(
            arrayOf(
                Child("A", parent),
                Child("B", parent),
                Child("C", parent)
            )
        )
        val saved = repository.save(parent)
        println(saved)
        assertNotNull(saved.id)
        saved.children.forEach { assertNotNull(it.id) }

        val found = repository.findById(saved.id!!).get()
        println(found)
        found.children.forEach { assertNotNull(it.parent) }

        val modifiedChildren = found.children.drop(1).map { it.copy(name = it.name + " mod!") }
        val modifiedParent = found.copy(name = found.name + " mod!", children = modifiedChildren)
        repository.update(modifiedParent)

        val found2 = repository.findById(saved.id!!).get()
        println(found2)
        saved.children.forEach { assertTrue(it.name.endsWith(" mod!")) }
    }
}
