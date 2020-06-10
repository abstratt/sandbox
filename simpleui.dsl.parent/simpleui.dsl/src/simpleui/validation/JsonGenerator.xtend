package simpleui.validation

import value.BooleanValue
import value.DictionaryValue
import value.ListValue
import value.NumericValue
import value.Slot
import value.StringValue

public class JsonGenerator {
	    
    def dispatch generateValue(DictionaryValue metadata) {
    	if (metadata.slots == null || metadata.slots.empty)
    	    return '{}'
    	
    '''
{
    «generateSlots(metadata)»
}
    '''
    }
    
    def String generateSlots(DictionaryValue metadata) {
        '''«metadata.slots.map[generateSlot.toString.trim].join(",\n")»'''
    }
    
    private def generateSlot(Slot slot) {
    	val slotValue = slot.value
        '''"«slot.name»" : «generateValue(slotValue)»'''
    }
	
    def dispatch String generateValue(ListValue metadata) {
    	if (metadata.items == null)
    	    return '[]'
        '''
    [
        «metadata.items.map[generateValue(it).toString.trim].join(", ")»
    ]
        '''    
    }
    
    def dispatch generateValue(StringValue metadata) {
    	generateJSONString(metadata.primitiveValue)
    }
    
    static def generateJSONString(String originalValue) {
    	if (originalValue == null)
    	    return "null"
    	val asSingleLine = originalValue.replaceAll('\\s+', ' ').replace('\\t', '')?.trim()
    	val StringBuilder jsonStringBuilder = new StringBuilder
    	asSingleLine.toCharArray.forEach[ char c |
    	    if (c <= 0x7F) {
    	        jsonStringBuilder.append(c)
    	    } else {
                jsonStringBuilder.append("\\u")
                var hexString = Integer.toHexString(c)
                while (hexString.length < 4)
                    hexString = '0' + hexString
    	    	jsonStringBuilder.append(hexString)
	    	}
    	]
    	val asJsonString = jsonStringBuilder.toString 
        '''"«asJsonString»"'''
    }
    
    def dispatch generateValue(NumericValue metadata) {
        metadata.primitiveValue
    }
    
    def dispatch generateValue(BooleanValue metadata) {
        metadata.primitiveValue
    }
    
    def dispatch generateValue(Void void) {
        'null'
    }
}
