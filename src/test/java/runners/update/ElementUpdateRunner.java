package runners.update;

import com.intuit.karate.junit5.Karate;

public class ElementUpdateRunner {
    @Karate.Test
    Karate elementUpdate() {
        return Karate.run("classpath:inventory/update/element-update.feature");
    }
}