package simpleui

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.diagnostics.IDiagnosticConsumer
import org.eclipse.xtext.linking.impl.Linker
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.diagnostics.IDiagnosticProducer
import value.ValuePackage
import value.MetadataHolder

class SimpleUILinker extends Linker {
    
    override protected doLinkModel(EObject model, IDiagnosticConsumer consumer) {
        super.doLinkModel(model, consumer)
    }
    
    override protected setDefaultValueImpl(EObject obj, EReference ref, IDiagnosticProducer producer) {
        if (ref == ValuePackage.Literals.DICTIONARY_VALUE__OBJECT_CLASS) {
            val component = obj.eContainer as MetadataHolder
            obj.eSet(ref, component.metadataType)
        }
        super.setDefaultValueImpl(obj, ref, producer)
    }
    
}