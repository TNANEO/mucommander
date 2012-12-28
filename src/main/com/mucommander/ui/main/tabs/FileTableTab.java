/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2012 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mucommander.ui.main.tabs;

import com.mucommander.commons.file.FileURL;
import com.mucommander.core.LocalLocationHistory;
import com.mucommander.ui.tabs.Tab;

/**
 * Interface of tab in the {@link com.mucommander.ui.main.FolderPanel} that contains a {@link com.mucommander.ui.main.table.FileTable}
 *
 * @author Arik Hadas
 */
public interface FileTableTab extends Tab {

	/**
	 * Setter for the location presented in the tab
	 * 
	 * @param location the file that is going to be presented in the tab
	 */
	public void setLocation(FileURL location);

	/**
	 * Getter for the location presented in the tab
	 * 
	 * @return the file that is being presented in the tab
	 */
	public FileURL getLocation();
	
	/**
	 * Set the tab to be locked or unlocked according to the given flag
	 * 
	 * @param locked flag that indicates whether the tab should be locked or not
	 */
	public void setLocked(boolean locked);
	
	/**
	 * Returns whether the tab is locked
	 * 
	 * @return indication whether the tab is locked
	 */
	public boolean isLocked();

	/**
	 * Set the title of the tab to the given string
	 * 
	 * @param title - predefined title to be assigned to the tab, null for no predefined title
	 */
	public void setTitle(String title);

	/**
	 * Returns the title that was assigned for the tab
	 * 
	 * @return the title that was assigned for the tab, null is returned if no title was assigned
	 */
	public String getTitle();

	/**
	 * Returns the tracker of the last accessed locations within the tab
	 * 
	 * @return tracker of the last accessed locations within the tab
	 */
	public LocalLocationHistory getLocationHistory();
}
