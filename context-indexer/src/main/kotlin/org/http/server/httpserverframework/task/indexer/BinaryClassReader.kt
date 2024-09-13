package org.http.server.httpserverframework.task.indexer

import java.io.DataInputStream
import java.io.FileInputStream
import java.io.IOException


object BinaryClassReader {
    fun getClassAnnotations(classFilePath: String): List<String> {
        DataInputStream(FileInputStream(classFilePath)).use { dis ->
            if (dis.readInt() != 0xCAFEBABE.toInt()) {
                throw IOException("Invalid class file")
            }

            // Skip minor_version and major_version
            dis.skipBytes(4)

            val constantPool = parseConstantPool(dis)
            dis.skipBytes(6) // access_flags, this_class, super_class

            val interfacesCount = dis.readUnsignedShort()
            dis.skipBytes(2 * interfacesCount) // interfaces

            val fieldsCount = dis.readUnsignedShort()
            for (i in 0 until fieldsCount) {
                dis.skipBytes(6) // access_flags, name_index, descriptor_index
                skipAttributes(dis) // field_attributes
            }

            val methodsCount = dis.readUnsignedShort()
            for (i in 0 until methodsCount) {
                dis.skipBytes(6) // access_flags, name_index, descriptor_index
                skipAttributes(dis) // method_attributes
            }

            val attributesCount = dis.readUnsignedShort()
            val annotations = mutableListOf<String>()
            for (i in 0 until attributesCount) {
                val attributeNameIndex = dis.readUnsignedShort()
                val attributeLength = dis.readInt()
                val attributeName = constantPool[attributeNameIndex] as String
                if (attributeName == "RuntimeVisibleAnnotations" || attributeName == "RuntimeInvisibleAnnotations") {
                    val numAnnotations = dis.readUnsignedShort()
                    for (j in 0 until numAnnotations) {
                        val annotationIndex = dis.readUnsignedShort()
                        val annotationDescriptor = constantPool[annotationIndex] as String
                        annotations.add(annotationDescriptor)
                        skipAnnotation(dis) // Skip the annotation content
                    }
                } else {
                    dis.skipBytes(attributeLength)
                }
            }

            return annotations
        }
    }

    private fun parseConstantPool(dis: DataInputStream): Array<Any?> {
        val constantPoolCount = dis.readUnsignedShort()
        val constantPool = arrayOfNulls<Any>(constantPoolCount)

        var i = 1
        while (i < constantPoolCount) {
            val tag = dis.readUnsignedByte()
            when (tag) {
                7, 8 -> dis.skipBytes(2) // Class, String
                9, 10, 11, 12 -> dis.skipBytes(4) // Fieldref, Methodref, InterfaceMethodref, NameAndType
                3, 4 -> dis.skipBytes(4) // Integer, Float
                5, 6 -> {
                    dis.skipBytes(8) // Long, Double
                    i++ // Note: Long and Double take two entries in the constant pool
                }
                1 -> {
                    val length = dis.readUnsignedShort()
                    val bytes = ByteArray(length)
                    dis.readFully(bytes)
                    constantPool[i] = String(bytes, Charsets.UTF_8)
                }
                15 -> dis.skipBytes(3) // MethodHandle
                16 -> dis.skipBytes(2) // MethodType
                18 -> dis.skipBytes(4) // InvokeDynamic
                else -> throw IllegalArgumentException("Unknown constant pool tag: $tag")
            }
            i++
        }

        return constantPool
    }

    private fun skipAttributes(dis: DataInputStream) {
        val attributesCount = dis.readUnsignedShort()
        for (i in 0 until attributesCount) {
            dis.skipBytes(2) // attribute_name_index
            val attributeLength = dis.readInt()
            dis.skipBytes(attributeLength)
        }
    }

    private fun skipAnnotation(dis: DataInputStream) {
        val numElementValuePairs = dis.readUnsignedShort()
        for (i in 0 until numElementValuePairs) {
            dis.skipBytes(2) // element_name_index
            skipElementValue(dis)
        }
    }

    private fun skipElementValue(dis: DataInputStream) {
        when (val tag = dis.readUnsignedByte()) {
            'B'.code, 'C'.code, 'D'.code, 'F'.code, 'I'.code, 'J'.code, 'S'.code, 'Z'.code, 's'.code -> dis.skipBytes(2)
            'e'.code -> dis.skipBytes(4)
            'c'.code -> dis.skipBytes(2)
            '@'.code -> skipAnnotation(dis)
            '['.code -> {
                val numValues = dis.readUnsignedShort()
                for (i in 0 until numValues) {
                    skipElementValue(dis)
                }
            }
            else -> throw IllegalArgumentException("Unknown element value tag: $tag")
        }
    }
}