package simpleui

import org.eclipse.xtext.linking.impl.DefaultLinkingService
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.linking.impl.IllegalNodeException
import value.ValuePackage

class SimpleUILinkingService extends DefaultLinkingService {

    override getLinkedObjects(EObject context, EReference ref, INode node) throws IllegalNodeException {
        if (context.eClass == ValuePackage.Literals.SLOT) {
            println(ref.name + " = " + node)
        }
        println(">>>")
        println(context)
        println(ref.name + " = " + node)
        println("<<<")
        val result = super.getLinkedObjects(context, ref, node)
        return result
    }
    
}