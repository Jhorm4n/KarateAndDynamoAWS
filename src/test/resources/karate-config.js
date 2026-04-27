function fn() {

    karate.configure('afterScenario', function () {
        var ddb = karate.get('ddb');
        if (ddb) {
          karate.log('Closing DynamoHelper connection');
          try {
            ddb.close();
          } catch (e) {
            karate.log('Error while closing DynamoHelper:', e);
          }
        }
     });

    var cfg = {
        aws: {
            key:    karate.properties['AWS_ACCESS_KEY_ID']     || java.lang.System.getenv('AWS_ACCESS_KEY_ID'),
            secret: karate.properties['AWS_SECRET_ACCESS_KEY'] || java.lang.System.getenv('AWS_SECRET_ACCESS_KEY'),
            region: karate.properties['AWS_REGION']            || java.lang.System.getenv('AWS_REGION') || 'us-east-2',
            table:  karate.properties['DDB_TABLE']             || java.lang.System.getenv('DDB_TABLE') || 'inventory'
        }
    };
    if (!cfg.aws.key || !cfg.aws.secret) {
        karate.fail('Faltan credenciales AWS (AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY)');
    } else {
        karate.log('karate-config.js CARGADO ✔');
    }
    return cfg;
}
