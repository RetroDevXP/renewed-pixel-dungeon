/*
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.retrodevxp.utils;

import java.util.Arrays;
import java.util.LinkedList;

import com.retrodevxp.utils.PathFinder;

public class PathFinder {
	
	public static int[] distance;
	
	private static boolean[] goals;
	private static int[] queue;
	
	private static int size = 0;

	private static int[] dir;
	
	public static void setMapSize( int width, int height ) {
		
		int size = width * height;
		
		if (PathFinder.size != size) {
			
			PathFinder.size = size;
			distance = new int[size];
			goals = new boolean[size];
			queue = new int[size];
			
			dir = new int[]{-1, +1, -width, +width, -width-1, -width+1, +width-1, +width+1};
		}
	}
	
	public static Path find( int from, int to, boolean[] passable ) {

		if (!buildDistanceMap( from, to, passable )) {
			return null;
		}
		
		Path result = new Path();
		int s = from;

		// From the starting position we are moving downwards, 
		// until we reach the ending point
		do {
			int minD = distance[s];
			int mins = s;
			
			for (int i=0; i < dir.length; i++) {
				
				int n = s + dir[i];
				
				int thisD = distance[n];
				if (thisD < minD) {
					minD = thisD;
					mins = n;
				}
			}
			s = mins;
			result.add( s );
		} while (s != to);
		
		return result;
	}
	
	public static int getStep( int from, int to, boolean[] passable ) {
		
		if (!buildDistanceMap( from, to, passable )) {
			return -1;
		}
		
		// From the starting position we are making one step downwards
		int minD = distance[from];
		int best = from;
		
		int step, stepD;
		
		for (int i=0; i < dir.length; i++) {

			if ((stepD = distance[step = from + dir[i]]) < minD) {
				minD = stepD;
				best = step;
			}
		}

		return best;
	}
	
	public static int getStepBack( int cur, int from, boolean[] passable ) {

		int d = buildEscapeDistanceMap( cur, from, 2f, passable );
		for (int i=0; i < size; i++) {
			goals[i] = distance[i] == d; 
		}
		if (!buildDistanceMap( cur, goals, passable )) {
			return -1;
		}

		int s = cur;
		
		// From the starting position we are making one step downwards
		int minD = distance[s];
		int mins = s;
		
		for (int i=0; i < dir.length; i++) {

			int n = s + dir[i];
			int thisD = distance[n];
			
			if (thisD < minD) {
				minD = thisD;
				mins = n;
			}
		}

		return mins;
	}
	
	private static boolean buildDistanceMap( int from, int to, boolean[] passable ) {
		
		if (from == to) {
			return false;
		}
		
		Arrays.fill( distance, Integer.MAX_VALUE );
		
		boolean pathFound = false;
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			if (step == from) {
				pathFound = true;
				break;
			}
			int nextDistance = distance[step] + 1;
			
			for (int i=0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n == from || (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance))) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
		
		return pathFound;
	}
	
	public static void buildDistanceMap( int to, boolean[] passable, int limit ) {
		
		Arrays.fill( distance, Integer.MAX_VALUE );
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			
			int nextDistance = distance[step] + 1;
			if (nextDistance > limit) {
				return;
			}
			
			for (int i=0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance)) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
	}
	
	private static boolean buildDistanceMap( int from, boolean[] to, boolean[] passable ) {
		
		if (to[from]) {
			return false;
		}
		
		Arrays.fill( distance, Integer.MAX_VALUE );
		
		boolean pathFound = false;
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		for (int i=0; i < size; i++) {
			if (to[i]) {
				queue[tail++] = i;
				distance[i] = 0;
			}
		}
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			if (step == from) {
				pathFound = true;
				break;
			}
			int nextDistance = distance[step] + 1;
			
			for (int i=0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n == from || (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance))) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
		
		return pathFound;
	}
	
	private static int buildEscapeDistanceMap( int cur, int from, float factor, boolean[] passable ) {
		
		Arrays.fill( distance, Integer.MAX_VALUE );
		
		int destDist = Integer.MAX_VALUE;
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		queue[tail++] = from;
		distance[from] = 0;
		
		int dist = 0;
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			dist = distance[step];
			
			if (dist > destDist) {
				return destDist;
			}
			
			if (step == cur) {
				destDist = (int)(dist * factor) + 1;
			}
			
			int nextDistance = dist + 1;
			
			for (int i=0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n >= 0 && n < size && passable[n] && distance[n] > nextDistance) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
		
		return dist;
	}
	
	@SuppressWarnings("unused")
	private static void buildDistanceMap( int to, boolean[] passable ) {
		
		Arrays.fill( distance, Integer.MAX_VALUE );
		
		int head = 0;
		int tail = 0;
		
		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;
		
		while (head < tail) {
			
			// Remove from queue
			int step = queue[head++];
			int nextDistance = distance[step] + 1;
			
			for (int i=0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance)) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
					
			}
		}
	}
	
	@SuppressWarnings("serial")
	public static class Path extends LinkedList<Integer> {
	}
}
