package runners.delete;

import com.intuit.karate.junit5.Karate;

public class ElementDeleteRunner {

    @Karate.Test
    Karate userGet(){
        return Karate.run("element-delete").relativeTo(getClass());
    }
}

