package com.popovanton0.blueprint

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.popovanton0.blueprint.dsl.Anchor
import com.popovanton0.blueprint.dsl.BlueprintBuilderScope
import com.popovanton0.blueprint.dsl.Dimension
import com.popovanton0.blueprint.dsl.MeasureUnit
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.End
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.Start
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Bottom
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Top
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
internal class Dsl {

    @Suppress("JUnitMalformedDeclaration")
    @Test
    fun `empty dsl produces empty blueprint`(
        @TestParameter("0", "1", "2") horizontalBlockCalls: Int,
        @TestParameter("0", "1", "2") verticalBlockCalls: Int,
        @TestParameter("0", "1", "2") horizontalGroupBlockCalls: Int,
        @TestParameter("0", "1", "2") verticalGroupBlockCalls: Int,
    ) {
        println("Blueprint")
        val actual = BlueprintBuilder {
            repeat(horizontalBlockCalls) {
                widths {
                    println("   horizontal")
                    repeat(horizontalGroupBlockCalls) {
                        group { }
                        println("       group")
                    }
                }
            }
            repeat(verticalBlockCalls) {
                heights {
                    println("   vertical")
                    repeat(verticalGroupBlockCalls) {
                        group { }
                        println("       group")
                    }
                }
            }
        }
        println("=======================\n")
        assertTrue(actual.groupCollection.flatMap { it }.isEmpty())
    }

    @Test
    fun `typical dsl usage`() {
        val actual = BlueprintBuilder {
            widths {
                group {
                    "one" lineTo "two"
                    "two".center lineTo "three".right
                }
                group(Top) {
                    "Apple" lineTo "Banana"
                    "Mango".center lineTo "Avocado".right
                }
            }
            heights {
                group {
                    "Car" lineTo "Bike"
                    "Plane".center lineTo "Helicopter".bottom
                }
                group(End) {
                    "Sock" lineTo "Blouse"
                    "Shirt".center lineTo "Bow-tie".bottom
                }
            }
        }

        assertEquals(Bottom, actual.horizontalBottomGroups.single().position)
        assertEquals(Top, actual.horizontalTopGroups.single().position)
        assertEquals(Start, actual.verticalLeftGroups.single().position)
        assertEquals(End, actual.verticalRightGroups.single().position)

        assertEquals(
            Dimension(Anchor("one", 0f), Anchor("two", 1f), MeasureUnit.Dp),
            actual.horizontalBottomGroups.single().dimensions[0],
        )
        assertEquals(
            Dimension(Anchor("Apple", 0f), Anchor("Banana", 1f), MeasureUnit.Dp),
            actual.horizontalTopGroups.single().dimensions[0],
        )
        assertEquals(
            Dimension(Anchor("Car", 0f), Anchor("Bike", 1f), MeasureUnit.Dp),
            actual.verticalLeftGroups.single().dimensions[0],
        )
        assertEquals(
            Dimension(Anchor("Sock", 0f), Anchor("Blouse", 1f), MeasureUnit.Dp),
            actual.verticalRightGroups.single().dimensions[0],
        )

        // second dimension

        assertEquals(
            Dimension(Anchor("two", 0.5f), Anchor("three", 1f), MeasureUnit.Dp),
            actual.horizontalBottomGroups.single().dimensions[1],
        )
        assertEquals(
            Dimension(Anchor("Mango", 0.5f), Anchor("Avocado", 1f), MeasureUnit.Dp),
            actual.horizontalTopGroups.single().dimensions[1],
        )
        assertEquals(
            Dimension(Anchor("Plane", 0.5f), Anchor("Helicopter", 1f), MeasureUnit.Dp),
            actual.verticalLeftGroups.single().dimensions[1],
        )
        assertEquals(
            Dimension(Anchor("Shirt", 0.5f), Anchor("Bow-tie", 1f), MeasureUnit.Dp),
            actual.verticalRightGroups.single().dimensions[1],
        )
    }

    @Suppress("TestFunctionName")
    private fun BlueprintBuilder(block: BlueprintBuilderScope.() -> Unit) =
        BlueprintBuilderScope().apply(block).toBlueprint()
}
