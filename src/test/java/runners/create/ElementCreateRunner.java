package runners.create;

import com.intuit.karate.junit5.Karate;

public class ElementCreateRunner {

    @Karate.Test
    Karate elementCreate(){ return Karate.run("classpath:inventory/create/element-create.feature"); }
}

