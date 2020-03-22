package me.sebbaindustries.bank;

import org.junit.Assert;
import org.junit.Test;

public class TestCore {

    @Test
    public void Temp() {

        final int expected = 4;

        final int actual = Math.addExact(2,2);

        Assert.assertEquals(actual, expected);
    }
}
