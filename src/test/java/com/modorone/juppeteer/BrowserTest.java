package com.modorone.juppeteer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * author: Shawn
 * time  : 4/20/20 6:12 PM
 * desc  :
 * update: Shawn 4/20/20 6:12 PM
 */
public class BrowserTest {


    @Test
    public void testNewPage() {
        System.out.println("hahha");
        Assert.assertThat(
                Arrays.asList("Java", "Kotlin", "Scala"),
                new Matcher<List<? super String>>() {
                    @Override
                    public boolean matches(Object item) {
                        return false;
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {

                    }

                    @Override
                    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                }
//                Assert.hasItems("Java", "Kotlin")
        );
        assert true;
    }
}
