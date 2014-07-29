// Copyright (c) 2014 K Team. All Rights Reserved.
package org.kframework.backend.java.symbolic;

import org.kframework.backend.java.kil.BuiltinMap;
import org.kframework.backend.java.kil.KItem;
import org.kframework.backend.java.kil.KList;
import org.kframework.backend.java.kil.MapLookup;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds map patterns candidates for expansion.
 *
 * @see org.kframework.backend.java.symbolic.PatternExpander
 *
 * @author AndreiS
 */
public class PatternTriggerFinder extends BottomUpVisitor {

    public final List<KItem> patterns = new ArrayList<>();

    @Override
    public void visit(MapLookup mapLookup) {
        if (!(mapLookup.map() instanceof BuiltinMap)) {
            return;
        }
        for (KItem pattern : ((BuiltinMap) mapLookup.map()).collectionPatterns()) {
            // TODO(AndreiS): refine to only consider input parameters
            if (((KList) pattern.kList()).getContents().contains(mapLookup.key())) {
                patterns.add(pattern);
            }
        }
    }

}
