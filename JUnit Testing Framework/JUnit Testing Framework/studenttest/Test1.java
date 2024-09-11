package studenttest;

import unittest.annotations.Test;
import unittest.assertions.Assert;

public class Test1 {
    @Test
    public void f () {
        Assert. assertTrue ( true );
    }

    @Test
    public void z () {
        Assert. assertTrue ( true );
    }
    @Test
    public void a () {
        Assert. assertTrue ( true );
    }
    @Test
    public void g () {
        Assert. assertTrue ( false );
    }
}
