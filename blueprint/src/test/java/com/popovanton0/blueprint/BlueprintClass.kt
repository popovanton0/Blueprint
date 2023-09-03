package com.popovanton0.blueprint

import com.popovanton0.blueprint.dsl.Anchor
import com.popovanton0.blueprint.dsl.Arrow
import com.popovanton0.blueprint.dsl.Blueprint
import com.popovanton0.blueprint.dsl.BlueprintBuilderScope
import com.popovanton0.blueprint.dsl.Dimension
import com.popovanton0.blueprint.dsl.HorizontalGroup
import com.popovanton0.blueprint.dsl.MeasureUnit
import com.popovanton0.blueprint.dsl.Position
import com.popovanton0.blueprint.dsl.SizeUnits
import com.popovanton0.blueprint.dsl.VerticalGroup
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

internal class BlueprintClass {
    @Test
    fun `basic equals-hashcode check`() {
        val original = BlueprintBuilderScope().apply {
            widths { group { "aaa" lineTo "bbb" } }
            heights { group { "bbb" lineTo "ccc" } }
        }.toBlueprint()
        val equal = BlueprintBuilderScope().apply {
            widths { group { "aaa" lineTo "bbb" } }
            heights { group { "bbb" lineTo "ccc" } }
        }.toBlueprint()
        val modified = BlueprintBuilderScope().apply {
            widths { group { "aaa" lineTo "bbb" } }
            heights { group { "bbb" lineTo "another non equal value" } }
        }.toBlueprint()

        assertEquals(original, equal)
        assertNotEquals(original, modified)
    }

    @Test
    fun `advanced equals-hashcode verification`() {
        EqualsVerifier.forClass(Blueprint::class.java).withIgnoredFields("groupCollection").verify()
        EqualsVerifier.forClasses(
            HorizontalGroup::class.java,
            VerticalGroup::class.java,
            Dimension::class.java,
            Position::class.java,
            Position.Horizontal::class.java,
            Position.Vertical::class.java,
            MeasureUnit::class.java,
            SizeUnits::class.java,
            Anchor::class.java,
            Arrow::class.java,
        ).verify()
    }
}