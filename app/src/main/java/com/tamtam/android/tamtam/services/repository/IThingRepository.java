/*
 *     Android application to create and display localized objects.
 *     Copyright (C) 2017  pascal bodin, antonin perrot-audet
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tamtam.android.tamtam.services.repository;

import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.ThingObject;

import java.util.Collection;

/**
 * Created by antoninpa on 28/01/17.
 */

public interface IThingRepository extends Repository<ThingObject> {

    /**
     * Get objects near a geographical position
     * @param position position around which we search for objects
     * @param maxDistanceInMeters maximum distance around position to look for things
     * @return null if not able to respond (empty cache or server conneciton issue),
     * {@link Collection<ThingObject>} with 0 or more thing objects.
     */
    Collection<ThingObject> getNear(PositionObject position, double maxDistanceInMeters);

}
