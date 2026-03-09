package com.example.library.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuActionTest {

    @Test
    void from_parsesTrimmedInput() {
        Assertions.assertEquals(MenuAction.DISPLAY, MenuAction.from(" 1 ").orElseThrow());
        Assertions.assertEquals(MenuAction.CREATE, MenuAction.from("2").orElseThrow());
        Assertions.assertEquals(MenuAction.EXIT, MenuAction.from("0").orElseThrow());
    }

    @Test
    void from_unknownReturnsEmpty(){
        Assertions.assertTrue(MenuAction.from("999").isEmpty());
        Assertions.assertTrue(MenuAction.from("x").isEmpty());
        Assertions.assertTrue(MenuAction.from(null).isEmpty());
    }
}
