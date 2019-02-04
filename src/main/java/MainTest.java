public class MainTest {
    @BeforeSuite
    public void befor() {
        System.out.println("befor");
    }

    @AfterSuite
    public void after() {
        System.out.println("after");
    }

    @Test(priority = 5)
    public void method1() {
        System.out.println("test with priority = 5");
    }

    @Test(priority = 1)
    public void method2() {
        System.out.println("test with priority = 1");
    }

    @Test(priority = 2)
    public void method3() {
        System.out.println("test with priority = 2");
    }
}
