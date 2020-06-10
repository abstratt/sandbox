package simpleui.validation

import java.io.File
import java.net.URI
import java.util.HashSet
import org.eclipse.core.runtime.FileLocator
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.validation.ValidationMessageAcceptor
import value.DictionaryValue
import value.ListValue
import value.PrimitiveValue
import value.Slot
import value.StringValue
import value.Value
import value.ValueFactory
import value.ValuePackage.Literals

class ValueValidationUtils {
    private var ValidationMessageAcceptor messageAcceptor
    private val DataFactory dataFactory = new DataFactory  
    
    
    new(ValidationMessageAcceptor messageAcceptor) {
        this.messageAcceptor = messageAcceptor
    }
    
    def void validateDictionary(EClass dictionaryType, DictionaryValue dictionary) {
        if (dictionaryType != null) {
            EcoreUtil.resolve(dictionaryType, dictionary)
            dictionary.objectClass = dictionaryType
        }
        // validate required features that have no defaults are provided
        val entries = dictionary.slots.toMap[name]
        dictionaryType.getEAllStructuralFeatures.forEach [ feature |
            val slotName = feature.name
            val slot = entries.get(slotName)
            if (slot == null)
                if (feature.defaultValue == null) {
                    if (feature.required && (!(feature.EType instanceof EClass) || !canDefault(feature.EType as EClass)))    
                        error('''Slot «slotName» is required''', dictionary, null)
                } else {
                    val newSlot = ValueFactory.eINSTANCE.createSlot()
                    newSlot.name = slotName
                    //TODO should create the value of the proper type here
                    newSlot.value = dataFactory.generateDefaultJSONValue(feature, feature.many)
                    dictionary.slots.add(newSlot)
                }
        ]

        // validate slots refer to features that exist and with the right type
        dictionary.slots.forEach [ slot, index |
            val correspondingFeature = dictionaryType.getEStructuralFeature(slot.name)
            if (correspondingFeature == null)
                warning('''Slot «slot.name» is unknown''', dictionary, Literals.DICTIONARY_VALUE__SLOTS,
                    index)
            else {
                if (correspondingFeature != null)
                    slot.slotFeature = correspondingFeature
                validateValue(correspondingFeature.getEType, correspondingFeature.required, correspondingFeature.many,
                    slot.value, slot)
            }
        ]
    }
    
    protected def void error(String message, EObject eObject, EStructuralFeature feature) {
        error(message, eObject, feature,  ValidationMessageAcceptor.INSIGNIFICANT_INDEX)
    }
    
    protected def void warning(String message, EObject eObject, EStructuralFeature feature) {
        warning(message, eObject, feature,  ValidationMessageAcceptor.INSIGNIFICANT_INDEX)
    }
    
    protected def void warning(String message, EObject eObject, EStructuralFeature feature, int index) {
        messageAcceptor.acceptWarning(message, eObject, feature,  index, null)
    }
    
    protected def void error(String message, EObject eObject, EStructuralFeature feature, int index) {
        messageAcceptor.acceptError(message, eObject, feature,  index, null)
    }

    /**
     * @param contextSlot the closest slot, we may be deeper than it though (imagine an array, the array slot will be the context for message errors for the item values)
     */
    private def void validateValue(EClassifier featureType, boolean required, boolean many, Value value,
        Slot contextSlot) {
        if (value == null) {
            if (required) {
                if (featureType instanceof EClass) {
                    if (canDefault(featureType as EClass))
                        // it is required but all required slots have defaults
                        return  
                }
                error('''Slot «contextSlot.name» is required, null/empty is not a proper value''', contextSlot,
                    Literals.SLOT__VALUE)
            }
            return
        }
        
        switch (value) {
            StringValue: {
                val basicValueFeature = value.eClass.getEStructuralFeature('primitiveValue')
                if (basicValueFeature.getEType != EcorePackage.Literals.ESTRING && basicValueFeature.getEType != Literals.LOCATION)
                    error(
                        '''Slot expects values of type «featureType.name», actual type «basicValueFeature.
                            getEType.name»''', contextSlot, Literals.SLOT__VALUE)
                if (featureType == Literals.LOCATION)
                    validateLocation(contextSlot, value)
            }
            PrimitiveValue: {
                val basicValueFeature = value.eClass.getEStructuralFeature('primitiveValue')
                if (basicValueFeature.getEType != featureType)
                    error(
                        '''Slot expects values of type «featureType.name», actual type «basicValueFeature.
                            getEType.name»''', contextSlot, Literals.SLOT__VALUE)
            }
            DictionaryValue: {
                if (featureType instanceof EClass)
                    validateDictionary(featureType as EClass, value)
                else
                    error('''Slot «contextSlot.name» expects values of type «featureType.name»''', contextSlot,
                        Literals.SLOT__VALUE)
            }
            ListValue: {
                if (!many)
                    error('''An array is expected for «contextSlot.name»''', contextSlot, Literals.SLOT__VALUE)
                value.items?.forEach [ validateValue(featureType, false, false, it, contextSlot) ]
            }
            default:
                warning(
                    '''Missed this case: slot «contextSlot.name» : «value?.eClass?.name»''', contextSlot, Literals.SLOT__VALUE)
        }
    }
    
    private def canDefault(EClass asEClass) {
        !asEClass.EAllStructuralFeatures.exists[required && defaultValue == null]
    }
    private def validateLocation(Slot contextSlot, StringValue value) {
        val baseURI = URI.create(contextSlot.eResource.URI.toString)
        val location = baseURI.resolve(value.primitiveValue)
        val resolved = FileLocator.resolve(location.toURL)
                            if (resolved.protocol == 'file' && !new File(resolved.path).file)
                                warning('''Could not find resource at "«value.primitiveValue»"''', contextSlot, Literals.SLOT__VALUE)
    }

    def void checkDictionary(DictionaryValue dictionary) {
        val inUse = new HashSet<String>()
        dictionary.slots.forEach [ slot, index |
            if (!inUse.add(slot.name))
                error('Duplicated slot name: ' + slot.name, dictionary,
                    Literals.DICTIONARY_VALUE__SLOTS, index)
        ]
    }    
}