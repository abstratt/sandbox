package simpleui.example

import simpleui.Application
import simpleui.impl.ApplicationImpl
import simpleui.SimpleuiPackage
import static extension org.eclipse.emf.ecore.util.EcoreUtil.create import simpleui.SimpleuiFactory

class ApplicationBuilder {
    def static Application build() {
        val application = SimpleuiFactory.eINSTANCE.createApplication
        return application
    }  
}