package elementix.dom.tags.fc

import elementix.dom.Prop
import elementix.dom.StaticProp
import elementix.dom.tags.Component
import elementix.reactivity.Context
import org.w3c.dom.Element

class For<T>(cx: Context,
             propsConfig: Props<T>.() -> Unit
) : DynamicChildrenComponent<For.Props<T>>(cx, propsConfig) {
    class Props<T> {
        var each: Prop<out List<T>> = StaticProp(listOf())
        var generator: Prop<((T) -> Component<*>)?> = StaticProp(null)
    }

    override fun getNodes(): List<Component<*>> {
        return if(props.generator.data != null) {
            props.each.data.map(props.generator.data!!)
        } else {
            listOf()
        }
    }

    override val props: Props<T> =
        Props<T>().apply(propsConfig)
}