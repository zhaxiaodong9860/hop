/*! ******************************************************************************
 *
 * Hop : The Hop Orchestration Platform
 *
 * http://www.project-hop.org
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

package org.apache.hop.ui.core.widget;

import org.apache.hop.ui.core.PropsUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class TreeUtil {
  public static final void setOptimalWidthOnColumns( Tree tree ) {
    int nrCols = tree.getColumnCount();
    int[] max = new int[ nrCols ];
    Canvas dummy_canvas = new Canvas( tree, SWT.NO_REDRAW_RESIZE );
    GC gc = new GC( dummy_canvas );

    for ( int i = 0; i < max.length; i++ ) {
      TreeColumn treeColumn = tree.getColumn( i );
      Point point = gc.textExtent( treeColumn.getText() );
      max[ i ] = point.x;
    }

    getMaxWidths( tree.getItems(), max, gc );

    gc.dispose();
    dummy_canvas.dispose();

    for ( int i = 0; i < max.length; i++ ) {
      TreeColumn treeColumn = tree.getColumn( i );
      treeColumn.setWidth( max[ i ] + (int)(40* PropsUi.getInstance().getZoomFactor()) );
    }
  }

  private static final void getMaxWidths( TreeItem[] items, int[] max, GC gc ) {
    for ( int i = 0; i < items.length; i++ ) {
      for ( int c = 0; c < max.length; c++ ) {
        String string = items[ i ].getText( c );
        Point point = gc.textExtent( string );
        if ( point.x > max[ c ] ) {
          max[ c ] = point.x;
        }
      }
      getMaxWidths( items[ i ].getItems(), max, gc );
    }
  }

  public static final TreeItem findTreeItem( Tree tree, String[] path ) {
    TreeItem[] items = tree.getItems();
    for ( int i = 0; i < items.length; i++ ) {
      TreeItem treeItem = findTreeItem( items[ i ], path, 0 );
      if ( treeItem != null ) {
        return treeItem;
      }
    }
    return null;
  }

  private static final TreeItem findTreeItem( TreeItem treeItem, String[] path, int level ) {
    if ( treeItem.getText().equals( path[ level ] ) ) {
      if ( level == path.length - 1 ) {
        return treeItem;
      }

      TreeItem[] items = treeItem.getItems();
      for ( int i = 0; i < items.length; i++ ) {
        TreeItem found = findTreeItem( items[ i ], path, level + 1 );
        if ( found != null ) {
          return found;
        }
      }
    }
    return null;
  }
}
