package simpleui.validation

import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.EcorePackage
import value.PrimitiveValue
import value.Value
import value.ValueFactory
import value.ValuePackage

class DataFactory {
    def Value generateJSONObject(EClass clazz) {
        val instance = ValueFactory.eINSTANCE.createDictionaryValue()
        clazz.EAllStructuralFeatures.filter[f|isRegularFeature(f)].forEach [ feature |
            val slot = ValueFactory.eINSTANCE.createSlot()
            instance.slots.add(slot)
            slot.name = feature.name
            slot.value = generateDefaultJSONValue(feature, feature.many)
        ]
        return instance
    }

    protected def boolean isRegularFeature(EStructuralFeature feature) {
        val featureNsPrefix = feature.EContainingClass.EPackage.nsPrefix
        val featureTypeNsPrefix = feature.EType.EPackage.nsPrefix
        val result = (!(feature instanceof EReference) || !(feature as EReference).container) && featureTypeNsPrefix != EcorePackage.eNAME
        return result 
    }

    def Value generateDefaultJSONValue(EStructuralFeature feature, boolean many) {
        val featureType = feature.EType

        if (many) {
            val value = ValueFactory.eINSTANCE.createListValue
            var i = 0
            while (i == 0 || i < feature.lowerBound) {
                value.items.add(generateDefaultJSONValue(feature, false))
                i = i + 1
            }
            return value
        }
        switch (featureType) {
            EClass: {
                return generateJSONObject(featureType)
            }
            EDataType: {
                var PrimitiveValue value
                var conversionType = featureType

                // the type default, if the feature doesn't have a default
                var String defaultDefaultValueAsString
                switch featureType.classifierID {
                    case EcorePackage.Literals.EINT: {
                        value = ValueFactory.eINSTANCE.createNumericValue()
                        defaultDefaultValueAsString = "0"
                    }
                    case EcorePackage.Literals.EBOOLEAN: {
                        value = ValueFactory.eINSTANCE.createBooleanValue()
                        defaultDefaultValueAsString = "false"
                    }
                    case EcorePackage.Literals.ESTRING: {
                        value = ValueFactory.eINSTANCE.createStringValue()
                        defaultDefaultValueAsString = "a value for " + feature.name
                    }
                    case ValuePackage.Literals.LOCATION: {
                        value = ValueFactory.eINSTANCE.createStringValue()
                        defaultDefaultValueAsString = "course/en/images/image.png"
                        conversionType = EcorePackage.Literals.ESTRING
                    }
                    default: {
                        return null
                    }
                }
                val primitiveValue = conversionType.getEPackage().getEFactoryInstance().createFromString(conversionType,
                    feature.defaultValueLiteral ?: defaultDefaultValueAsString)
                value.eSet(value.eClass.getEStructuralFeature("primitiveValue"), primitiveValue)
                return value
            }
            default:
                throw new IllegalArgumentException(feature.toString)
        }
    }

}
