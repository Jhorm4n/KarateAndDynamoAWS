package runners.get;

import com.intuit.karate.junit5.Karate;

public class ElementGetRunner {

    @Karate.Test
    Karate elementGet(){
        return Karate.run("classpath:inventory/get/element-get.feature");
    }
}

