/**
 *
 * TURTLE PLAYER
 *
 * Licensed under MIT & GPL
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * More Information @ www.turtle-player.co.uk
 *
 * @author Simon Honegger (Hoene84)
 */

package turtle.player.playlist.filter;

import turtle.player.model.Track;

import java.util.*;

public class Filters
{
	Set<PlaylistFilter> filters = new HashSet<PlaylistFilter>();


	public Filters()
	{
		filters.add(PlaylistFilter.ALL);
	}

	public void addFilter(PlaylistFilter filter)
	{
		filters.add(filter);
		notifyObservers();
	}

	public void removeFilter(PlaylistFilter filter)
	{
		filters.remove(filter);
		notifyObservers();
	}

	public void removeAllFilters()
	{
		filters.clear();
		notifyObservers();
	}


	public Set<Track> getValidTracks(Collection<Track> candidates)
	{
		Set<Track> tracks = new HashSet<Track>();

		for (Track track : candidates)
		{
			if (isValidAccordingFilters(track))
			{
				tracks.add(track);
			}
		}

		return tracks;
	}

	public boolean isValidAccordingFilters(Track candidate)
	{
		return isValidAccordingFilters(candidate, filters);
	}

	public static boolean isValidAccordingFilters(Track track,
																 Set<PlaylistFilter> filters)
	{
		int force = 0;
		boolean accept = false;

		for (PlaylistFilter filter : filters)
		{
			if (filter.getForce() > force)
			{
				accept = filter.accept(track);
				force = filter.getForce();
			}
		}
		return accept;
	}

	// ========================================= //
	// 	Observable
	// ========================================= //

	List<FilterObserver> observers = new ArrayList<FilterObserver>();

	public interface FilterObserver
	{
		void filterChanged();
	}

	public static abstract class FilterObserverAdapter implements FilterObserver
	{
		@Override
		public void filterChanged()
		{
			//do nothing
		}
	}

	public void addObserver(FilterObserver observer)
	{
		observers.add(observer);
	}

	public void removeObserver(FilterObserver observer)
	{
		observers.remove(observer);
	}

	private void notifyObservers()
	{
		for (FilterObserver observer : observers)
		{
			observer.filterChanged();
		}
	}
}
