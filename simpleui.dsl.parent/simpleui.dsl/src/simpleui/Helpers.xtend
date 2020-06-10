package simpleui

import java.net.URL
import org.eclipse.core.runtime.FileLocator
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.common.util.WrappedException
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.Resource.Diagnostic
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.EcoreUtil2

class Helpers {
    
    static def URI translatePluginURI(String partialUri) {
        val pluginUrl = new URL("platform:/plugin" + partialUri)
//        val resolvedUrl = FileLocator.resolve(pluginUrl) 
//        return URI.createURI(resolvedUrl.toString)
        return URI.createURI(pluginUrl.toString)
    }    
    
    static def boolean isSuperType(EClass ancestor, EClass candidateDescendant) {
        if (EcoreUtil2.isAssignableFrom(ancestor, candidateDescendant))
            return true
        val ancestorURI = ancestor.eResource.getURIFragment(ancestor)
        val eClasses = #[candidateDescendant] + candidateDescendant.EAllSuperTypes
        val allSuperURIs = eClasses.map[candidate | candidate.eResource.getURIFragment(candidate)]
        val allSuperTypeNamess = eClasses.map[candidate | candidate.EPackage.name + '.' + candidate.name]
        val isSuperType = eClasses.exists[candidate | candidate.EPackage.name == ancestor.EPackage.name && candidate.name == ancestor.name]
        if (isSuperType) println('''«ancestor.name».isSuperType(«candidateDescendant.name») = «isSuperType»''')
        return isSuperType
    }
    
    def static EPackage findPackage(Resource referenceResource, String importedModuleName, boolean loadIfNeeded) {
        val set = referenceResource.resourceSet
        val extensions = #['xcore', 'ecore']
        val modelRelativeURIs = extensions.map[ext|URI.createURI(importedModuleName + "." + ext)]
        val pluginURIs = extensions.map[ext|translatePluginURI("/simpleui." + importedModuleName + "/" + importedModuleName + "." + ext)]
        val userAbsoluteURIs = if (referenceResource.URI.scheme == null) 
            modelRelativeURIs 
        else 
            modelRelativeURIs.map [
                it.resolve(referenceResource.URI)
            ]
        val existing = (pluginURIs + userAbsoluteURIs).map[safeGetResource(set, it, loadIfNeeded)].filterNull.map[it.findEPackage].filterNull
        if(!existing.empty) 
            return existing.head

        return set.resources.findFirst[it.URI.lastSegment == importedModuleName]?.findEPackage()
    }
    
    def static Resource safeGetResource(ResourceSet set, URI uri, boolean loadIfNeeded) {
        println(uri + " Load? " + loadIfNeeded)
        try {
            val result = set.getResource(uri, loadIfNeeded)
            println("Found")
            return result
        } catch (WrappedException e) {
            if (!(e instanceof Diagnostic))
                throw e
            println("Not found")
            return null
        }
        
    }
    
    
    static def EPackage findEPackage(Resource resource) {
        val ePackage = resource?.contents.findFirst[it.eClass == EcorePackage.Literals.EPACKAGE] as EPackage
        println("EPackage in " + resource.URI + "? " + (ePackage?.name))
        return ePackage
    }

    
}