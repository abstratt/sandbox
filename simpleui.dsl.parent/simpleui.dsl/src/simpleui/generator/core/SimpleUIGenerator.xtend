package simpleui.generator.core

import org.eclipse.emf.common.util.EList
import simpleui.Application
import simpleui.Component
import simpleui.Container
import value.DictionaryValue
import value.PrimitiveValue

class SimpleUIGenerator {
    
    def CharSequence generate(Application application) {
        '''
        <html>
        <head>
            <title>«application.name»</title>
        </head>
        <body>
        «FOR s : application.screens»
            <h1>Screen: «s.name»</h1>
            «s.children.showTree»
        «ENDFOR»
        </body>
        </html>
        '''        
    }
    
    def CharSequence showTree(EList<Component> list) {
        '''
        <ul>
            «list.map['''<li>«it.showComponent»</li>'''].join»
        </ul>
        '''
    }
    
    def dispatch CharSequence showComponent(Component component) {
        '''
        <div>
        «component.eClass.name» «component.name» : «component.componentType.name»
        «component.metadata?.showProperties»
        </div>
        '''
    }

    def dispatch CharSequence showComponent(Container component) {
        '''
        <div>
        «component.componentType.name» «component.name»
        «component.metadata?.showProperties»
        «component.children.showTree»
        </div>
        '''
    }
    
    def CharSequence showProperties(DictionaryValue metadata) {
        '''
        <ul>
        «metadata.slots.map['''<li>«it.slotFeature.name» : «it.slotFeature.EType.name» = «it.value.showValue»</li>'''].join("\\n")»
        </ul>
        '''
    }
    
    def dispatch CharSequence  showValue(PrimitiveValue value) {
        '''«value.primitiveValue»'''
    }
    
    def dispatch CharSequence  showValue(DictionaryValue value) {
        value.showProperties
    }
    
}