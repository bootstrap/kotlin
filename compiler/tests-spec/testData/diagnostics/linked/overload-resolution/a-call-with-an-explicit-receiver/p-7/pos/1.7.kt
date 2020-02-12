// !LANGUAGE: +NewInference
// !DIAGNOSTICS: -UNUSED_VARIABLE -ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE -UNUSED_VALUE -UNUSED_PARAMETER -UNUSED_EXPRESSION
// SKIP_TXT

/*
 * KOTLIN DIAGNOSTICS SPEC TEST (POSITIVE)
 *
 * SPEC VERSION: 0.1-261
 * PLACE: overload-resolution, a-call-with-an-explicit-receiver -> paragraph 7 -> sentence 1
 * NUMBER: 7
 * DESCRIPTION: The sets of non-extension member callables named f of type T;
 */

// FILE: Marker.kt
package libPackage

class Marker {
    fun foo() = println("non-extension member Marker.foo()")
    val foo: Marker = this
    operator fun invoke() =  println("non-extension member Marker.invoke()")
}


// TESTCASE NUMBER: 1

// FILE: Tests.kt
package tests
import libPackage.Marker

class Case1() {

    fun Marker.<!EXTENSION_SHADOWED_BY_MEMBER!>foo<!>() = println("local extension marker.foo")

    fun test() {
        Marker().<!DEBUG_INFO_AS_CALL("fqName: libPackage.Marker.foo; typeCall: function; ")!>foo()<!>
    }
}

fun case1() {
    Marker().<!DEBUG_INFO_AS_CALL("fqName: libPackage.Marker.foo; typeCall: function; ")!>foo()<!>
}

// TESTCASE NUMBER: 2
class Case2() {
    fun test() {
        fun Marker.<!EXTENSION_SHADOWED_BY_MEMBER!>foo<!>() = println("local extension marker.foo")
        Marker().<!DEBUG_INFO_AS_CALL("fqName: libPackage.Marker.foo; typeCall: function; ")!>foo()<!>
    }
}

fun Marker.<!EXTENSION_SHADOWED_BY_MEMBER!>foo<!>() = println("top level extension marker.foo")

fun case2() {
    Marker().<!DEBUG_INFO_AS_CALL("fqName: libPackage.Marker.foo; typeCall: function; ")!>foo()<!>
}

// TESTCASE NUMBER: 3
fun case3() {
    fun Marker.<!EXTENSION_SHADOWED_BY_MEMBER!>foo<!>() = println("local extension marker.foo")
    Marker().<!DEBUG_INFO_AS_CALL("fqName: libPackage.Marker.foo; typeCall: function; ")!>foo()<!>
}
