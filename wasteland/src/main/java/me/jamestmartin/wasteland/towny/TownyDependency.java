package me.jamestmartin.wasteland.towny;

/**
 * <p>
 * This plugin would like to be able to use features of Towny if it is available,
 * without being unable to function if Towny is not available.
 * <p>
 * Any class which imports Towny packages will not be possible to load if Towny is not available,
 * so instead, we restrict all Towny imports to one class, and decide whether or not to load it at runtime.
 * If Towny is not available, we will instead use a dummy class which does nothing and returns nothing.
 * <p>
 * We split the functionality of towny that one might want to access into various provider interfaces.
 * This interface collects them all into a single interface so that they might all be implemented
 * at once and loaded at once through the enabled/disabled implementation.
 * 
 * @see TownyDisabled The dummy implementation used if Towny is not available.
 * @see TownyEnabled The actual implementation used if Towny *is* available.
 */
public interface TownyDependency extends TownyTagProvider {

}
