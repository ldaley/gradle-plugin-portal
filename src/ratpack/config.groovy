import org.ratpackframework.app.Config

(this as Config).with { Config config ->
    staticallyCompileTemplates false
    onStart new Init()
}