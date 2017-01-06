/*
 * Java GPX Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.jpx.jdbc;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import io.jenetics.jpx.Bounds;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class BoundsDAO extends DAO {

	public BoundsDAO(final Connection connection) {
		super(connection);
	}

	/**
	 * The link row parser which creates a {@link Bounds} object from a given DB
	 * row.
	 */
	private static final RowParser<Stored<Bounds>> RowParser = rs -> Stored.of(
		rs.getLong("id"),
		Bounds.of(
			rs.getDouble("minlat"),
			rs.getDouble("minlon"),
			rs.getDouble("maxlat"),
			rs.getDouble("maxlon")
		)
	);

/*
	CREATE TABLE bounds(
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	minlat DOUBLE PRECISION NOT NULL,
	minlon DOUBLE PRECISION NOT NULL,
	maxlat DOUBLE PRECISION NOT NULL,
	maxlon DOUBLE PRECISION NOT NULL
);
	 */


	/* *************************************************************************
	 * INSERT queries
	 **************************************************************************/

	/**
	 * Insert the given bounds list into the DB.
	 *
	 * @param bounds the bounds to insert
	 * @return return the stored copyrights
	 * @throws SQLException if inserting fails
	 */
	public List<Stored<Bounds>> insert(final List<Bounds> bounds)
		throws SQLException
	{
		final String query =
			"INSERT INTO bounds(minlat, minlon, maxlat, maxlon) " +
			"VALUES({minlat}, {minlon}, {maxlat}, {maxlon})";

		return Batch(query).insert(bounds, bound -> asList(
			Param.value("minlat", bound.getMinLatitude().doubleValue()),
			Param.value("minlon", bound.getMinLongitude().doubleValue()),
			Param.value("maxlat", bound.getMaxLatitude().doubleValue()),
			Param.value("maxlon", bound.getMaxLongitude().doubleValue())
		));
	}

	/**
	 * Insert the given bounds into the DB.
	 *
	 * @param bounds the copyright to insert
	 * @return return the stored bounds
	 * @throws SQLException if inserting fails
	 */
	public Stored<Bounds> insert(final Bounds bounds)
		throws SQLException
	{
		return insert(singletonList(bounds)).get(0);
	}


	/* *************************************************************************
	 * SELECT queries
	 **************************************************************************/

	/**
	 * Select all available copyrights.
	 *
	 * @return all stored copyrights
	 * @throws SQLException if the select fails
	 */
	public List<Stored<Bounds>> select() throws SQLException {
		final String query =
			"SELECT id, minlat, minlon, maxlat, maxlon FROM bounds;";

		return SQL(query).as(RowParser.list());
	}


	public static BoundsDAO of(final Connection conn) {
		return new BoundsDAO(conn);
	}

}
