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

package com.tamtam.android.tamtam.services.json;

/**
 * This exception is raised when something bad happened trying to write Json.
 **/
public class MappingException extends Exception{
    public MappingException() {}
    public MappingException(String msg) { super(msg); }
    public MappingException(Throwable cause) { super(cause); }
    public MappingException(String msg, Throwable cause) { super(msg, cause); }
}
