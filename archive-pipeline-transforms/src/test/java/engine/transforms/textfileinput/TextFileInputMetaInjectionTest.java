/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.apache.hop.pipeline.transforms.fileinput;

import org.apache.hop.core.row.IValueMeta;
import org.apache.hop.pipeline.transform.TransformInjectionMetaEntry;
import org.apache.hop.pipeline.transform.TransformInjectionUtil;
import org.apache.hop.pipeline.transforms.loadsave.validator.FieldLoadSaveValidator;
import org.apache.hop.pipeline.transforms.loadsave.validator.IntLoadSaveValidator;
import org.apache.hop.pipeline.transforms.loadsave.validator.StringLoadSaveValidator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ACCEPT_FILE_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ACCEPT_FILE_NAMES;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ACCEPT_FILE_TRANSFORM;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ADD_FILES_TO_RESULT;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.BREAK_IN_ENCLOSURE;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.COMPRESSION_TYPE;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.DATE_FORMAT_LENIENT;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.DATE_FORMAT_LOCALE;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ENCLOSURE;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ENCODING;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ERROR_COUNT_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ERROR_FIELDS_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ERROR_FILES_EXTENTION;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ERROR_FILES_TARGET_DIR;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ERROR_LINES_SKIPPED;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ERROR_TEXT_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ESCAPE_CHAR;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILENAME_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_ERROR_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_ERROR_MESSAGE_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_EXTENSION_FIELDNAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_FORMAT;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_HIDDEN_FIELDNAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_LAST_MODIFICATION_FIELDNAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_PATH_FIELDNAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_SHORT_FILE_FIELDNAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_SIZE_FIELDNAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_TYPE;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.FILE_URI_FIELDNAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.HAS_FOOTER;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.HAS_PAGED_LAYOUT;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.HAS_WRAPPED_LINES;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.HEADER_PRESENT;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.IGNORE_ERRORS;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.INCLUDE_FILENAME;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.INCLUDE_ROW_NUMBER;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.LINE_NR_FILES_EXTENTION;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.LINE_NR_FILES_TARGET_DIR;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.NO_EMPTY_LINES;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.NR_DOC_HEADER_LINES;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.NR_FOOTER_LINES;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.NR_HEADER_LINES;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.NR_LINES_PER_PAGE;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.NR_WRAPS;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.PASS_THROUGH_FIELDS;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ROW_LIMIT;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ROW_NUMBER_BY_FILE;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.ROW_NUMBER_FIELD;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.SEPARATOR;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.SKIP_BAD_FILES;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.WARNING_FILES_EXTENTION;
import static org.apache.hop.pipeline.transforms.fileinput.TextFileInputMetaInjection.Entry.WARNING_FILES_TARGET_DIR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @deprecated replaced by implementation in the ...transforms.fileinput.text package
 */
public class TextFileInputMetaInjectionTest {

  @Test
  public void extractingAll() throws Exception {
    TextFileInputMetaInjection injection = new TextFileInputMetaInjection( new TextFileInputMeta() );
    List<TransformInjectionMetaEntry> metadata = injection.getTransformInjectionMetadataEntries();
    List<TransformInjectionMetaEntry> extracted = injection.extractTransformMetadataEntries();

    assertEquals( metadata.size(), extracted.size() );
    for ( TransformInjectionMetaEntry metaEntry : metadata ) {
      assertNotNull( metaEntry.getKey(), TransformInjectionUtil.findEntry( extracted, metaEntry.getKey() ) );
    }
  }


  @Test
  public void topEntriesAreInjected() throws Exception {
    TextFileInputMetaInjection.Entry[] topEntries = TextFileInputMetaInjection.Entry.getTopEntries();
    List<TransformInjectionMetaEntry> injectionValues = createInjectionValues( topEntries );

    TextFileInputMetaInjection injection = new TextFileInputMetaInjection( new TextFileInputMeta() );
    injection.injectTransformMetadataEntries( injectionValues );

    assertInjected( injection.extractTransformMetadataEntries(), injectionValues );
  }

  private static List<TransformInjectionMetaEntry> createInjectionValues( TextFileInputMetaInjection.Entry[] entries ) {
    Map<TextFileInputMetaInjection.Entry, Generator<?>> generators = createGeneratorsMapping();
    List<TransformInjectionMetaEntry> result = new ArrayList<TransformInjectionMetaEntry>( entries.length );
    for ( TextFileInputMetaInjection.Entry entry : entries ) {
      TransformInjectionMetaEntry injectionEntry = TransformInjectionUtil.getEntry( entry );
      if ( entry.getValueType() != IValueMeta.TYPE_NONE ) {
        injectionEntry.setValue( generators.get( entry ).generateValue() );
      }
      result.add( injectionEntry );
    }
    return result;
  }

  private static Map<TextFileInputMetaInjection.Entry, Generator<?>> createGeneratorsMapping() {
    Map<TextFileInputMetaInjection.Entry, Generator<?>> generators = new EnumMap<TextFileInputMetaInjection.Entry,
      Generator<?>>( TextFileInputMetaInjection.Entry.class );

    Generator<String> stringGenerator = new ValidatorAdapter<String>( new StringLoadSaveValidator() );
    List<TextFileInputMetaInjection.Entry> stringEntries = Arrays.asList(
      FILE_TYPE, SEPARATOR, ENCLOSURE, ESCAPE_CHAR, COMPRESSION_TYPE, FILENAME_FIELD, ROW_NUMBER_FIELD,
      FILE_FORMAT, ENCODING, ACCEPT_FILE_TRANSFORM, ACCEPT_FILE_FIELD, FILE_SHORT_FILE_FIELDNAME, FILE_PATH_FIELDNAME,
      FILE_LAST_MODIFICATION_FIELDNAME, FILE_URI_FIELDNAME, FILE_EXTENSION_FIELDNAME, FILE_SIZE_FIELDNAME,
      FILE_ERROR_FIELD, FILE_ERROR_MESSAGE_FIELD, ERROR_COUNT_FIELD, ERROR_FIELDS_FIELD, ERROR_TEXT_FIELD,
      WARNING_FILES_TARGET_DIR, WARNING_FILES_EXTENTION, ERROR_FILES_TARGET_DIR, ERROR_FILES_EXTENTION,
      LINE_NR_FILES_TARGET_DIR, LINE_NR_FILES_EXTENTION
    );
    for ( TextFileInputMetaInjection.Entry entry : stringEntries ) {
      generators.put( entry, stringGenerator );
    }

    Generator<String> intGenerator = new ValidatorAdapter<Integer>( new IntLoadSaveValidator() );
    List<TextFileInputMetaInjection.Entry> intEntries = Arrays.asList(
      NR_HEADER_LINES, NR_FOOTER_LINES, NR_WRAPS, NR_DOC_HEADER_LINES, NR_LINES_PER_PAGE, ROW_LIMIT
    );
    for ( TextFileInputMetaInjection.Entry entry : intEntries ) {
      generators.put( entry, intGenerator );
    }

    Generator<String> yesNoGenerator = new YesNoGenerator();
    List<TextFileInputMetaInjection.Entry> yesNoEntries = Arrays.asList(
      BREAK_IN_ENCLOSURE, HEADER_PRESENT, HAS_FOOTER, HAS_WRAPPED_LINES, HAS_PAGED_LAYOUT, NO_EMPTY_LINES,
      INCLUDE_FILENAME, INCLUDE_ROW_NUMBER, ROW_NUMBER_BY_FILE, DATE_FORMAT_LENIENT, ACCEPT_FILE_NAMES,
      PASS_THROUGH_FIELDS, ADD_FILES_TO_RESULT, FILE_HIDDEN_FIELDNAME, SKIP_BAD_FILES, IGNORE_ERRORS,
      ERROR_LINES_SKIPPED
    );
    for ( TextFileInputMetaInjection.Entry entry : yesNoEntries ) {
      generators.put( entry, yesNoGenerator );
    }

    generators.put( DATE_FORMAT_LOCALE, new Constant<String>( "en" ) );

    return generators;
  }

  private static void assertInjected( List<TransformInjectionMetaEntry> fields, List<TransformInjectionMetaEntry> toBeInjected ) {
    Map<String, TransformInjectionMetaEntry> map = new HashMap<String, TransformInjectionMetaEntry>( fields.size() );
    for ( TransformInjectionMetaEntry field : fields ) {
      map.put( field.getKey(), field );
    }

    for ( TransformInjectionMetaEntry entry : toBeInjected ) {
      TransformInjectionMetaEntry field = map.get( entry.getKey() );
      assertNotNull( entry.getKey(), field );

      Object value = field.getValue();
      if ( value == null ) {
        assertNull( entry.getKey(), entry.getValue() );
      } else {
        assertEquals( entry.getKey(), entry.getValue(), value );
      }
    }
  }


  private static interface Generator<T> {
    T generateValue();
  }

  private static class YesNoGenerator implements Generator<String> {
    private final Random random = new Random();

    @Override public String generateValue() {
      if ( random.nextBoolean() ) {
        return "Y";
      } else {
        return "N";
      }
    }
  }

  private static class ValidatorAdapter<T> implements Generator<String> {
    private final FieldLoadSaveValidator<T> validator;

    public ValidatorAdapter( FieldLoadSaveValidator<T> validator ) {
      this.validator = validator;
    }

    @Override public String generateValue() {
      return validator.getTestObject().toString();
    }
  }

  private static class Constant<T> implements Generator<T> {
    private final T value;

    public Constant( T value ) {
      this.value = value;
    }

    @Override public T generateValue() {
      return value;
    }
  }

}