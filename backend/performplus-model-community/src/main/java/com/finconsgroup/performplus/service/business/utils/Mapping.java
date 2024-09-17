package com.finconsgroup.performplus.service.business.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


public class Mapping {
	static ModelMapper mm = new ModelMapper();
	static {
		mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		mm.getConfiguration().setAmbiguityIgnored(true);
	}

	public static <D, E> void mapping(final D from, final E to) {
		if (from == null || to == null) {
			return;
		}
		mm.map(from, to);
	}

	public static <D, E> void mapping(final Optional<D> from, final E to) {
		if (from.isEmpty() || to == null) {
			return;
		}
		mm.map(from.get(), to);
	}

	public static <D, E> E mapping(final D from, final Class<E> c) {
		if (from == null) {
			return null;
		}

		E to;
		try {
			to = c.getDeclaredConstructor().newInstance();
			mm.map(from, to);
		} catch (Throwable e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return to;
	}

	public static <D, E> E mapping(final Optional<D> from, final Class<E> c) {
		if (from.isEmpty()) {
			return null;
		}
		E to;
		try {
			to = c.getDeclaredConstructor().newInstance();
		} catch (Throwable e) {
			System.err.println(e.getMessage());
			return null;
		}
		mm.map(from.get(), to);
		return to;
	}


	public static <D, E> List<E> mapping(final List<D> from, final Class<E> c) {
		List<E> out = new ArrayList<>();
		if (from == null)
			return out;
		for (D r : from) {
			out.add(mapping(r, c));
		}
		return out;
	}


}
