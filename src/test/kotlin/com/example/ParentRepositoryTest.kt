package com.example

import com.example.domain.Child
import com.example.domain.Parent
import com.example.domain.ParentRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
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
        // Fixture
        val children = mutableListOf<Child>()
        val parent = Parent("parent", children)
        children.addAll(
            listOf(
                Child("A", parent),
                Child("B", parent),
                Child("C", parent)
            )
        )

        // Save relation
        val saved = repository.save(parent)

        // Ensure parent and all children has an id generated by the database
        val savedId = saved.id!!
        saved.children.forEach { assertNotNull(it.id) }

        // Retrieve parent from database, and ensures all children has a valid parent
        val found = repository.findById(savedId).get()
        found.children.forEach {
            assertNotNull(it.parent)
            assertEquals(savedId, it.parent!!.id)
        }

        // Modify children and save parent to save the changes
        val modifiedChildren = found.children.map { it.copy(name = it.name + " mod!") }
        val modifiedParent = found.copy(name = found.name + " mod!", children = modifiedChildren)
        repository.update(modifiedParent)

        // Retrieve parent from database and ensure modified children
        val foundAfterModification = repository.findById(savedId).get()
        foundAfterModification.children.forEach { assertTrue(it.name.endsWith(" mod!"), "Expected modified child") }
    }
}
