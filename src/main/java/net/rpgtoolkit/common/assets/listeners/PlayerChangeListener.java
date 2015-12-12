/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.listeners;

import java.util.EventListener;
import net.rpgtoolkit.common.assets.events.PlayerChangedEvent;

/**
 * Implementors of this interface will use the contained method definitions to inform their 
 * listeners of new event on a <code>Player</code>.
 * 
 * @author Joshua Michael Daly
 */
public interface PlayerChangeListener  extends EventListener  {
  
  /**
     * A general player changed event.
     * 
     * @param e
     */
    public void playerChanged(PlayerChangedEvent e);

}
