package runners.create;

import com.intuit.karate.junit5.Karate;

public class ElementCreateRunner {

    @Karate.Test
    Karate userGet(){
        return Karate.run("element-create").relativeTo(getClass());
    }
}

