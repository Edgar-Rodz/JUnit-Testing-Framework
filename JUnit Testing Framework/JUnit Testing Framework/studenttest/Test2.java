package studenttest;

import unittest.annotations.Order;
import unittest.annotations.Ordered;
import unittest.annotations.Test;
import unittest.assertions.Assert;

@Ordered
public class Test2 {
    @Test
    @Order(3)
    public void testA() {
        Assert.assertEquals(5, 5);
    }
    @Test
    @Order(2)
    public void testC() {
        Assert.assertEquals(6, 1 + 2);
    }
    @Test
    public void ctest() {
        Assert.assertEquals(3, 3);
    }
    @Test
    @Order(1)
    public void atest() {
        Assert.assertEquals(3, 3);
    }

    @Test
    public void btest() {
        Assert.assertEquals(3, 3);
    }
}
