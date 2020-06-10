package simpleui.validation

import simpleui.SimpleuiPackage
import org.eclipse.emf.ecore.EClass

class SimpleUILanguageUtils {
    static def EClass findComponentType(String typeName) {
        SimpleuiPackage.eINSTANCE.getEClassifier(typeName.toFirstUpper) as EClass
    }
}