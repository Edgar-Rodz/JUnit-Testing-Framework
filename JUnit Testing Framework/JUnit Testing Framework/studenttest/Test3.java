package studenttest;

import unittest.annotations.Order;
import unittest.annotations.Ordered;
import unittest.annotations.Test;
import unittest.assertions.Assert;
@Ordered
public class Test3 {

    public void test4() {
        Assert.assertEquals(4, 3);
    }
    @Test
    public void test5() {
        Assert.assertEquals(5, 3 + 2);
    }
    @Test
    public void test6() {
        Assert.assertEquals(6, 6);
    }

    @Order(7)
    public void test7(){Assert.assertTrue(true);}

}
