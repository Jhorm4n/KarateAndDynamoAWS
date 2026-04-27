package runners.delete;

import com.intuit.karate.junit5.Karate;

public class ElementDeleteRunner {

    @Karate.Test
    Karate elementDelete(){
        return Karate.run("classpath:inventory/delete/element-delete.feature");
    }
}

