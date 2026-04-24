package runners.get;

import com.intuit.karate.junit5.Karate;

public class ElementGetRunner {

    @Karate.Test
    Karate userGet(){
        return Karate.run("element-get").relativeTo(getClass());
    }
}

