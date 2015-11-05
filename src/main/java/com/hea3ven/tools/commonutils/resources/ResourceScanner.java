package com.hea3ven.tools.commonutils.resources;

import java.io.InputStream;

public interface ResourceScanner {
	Iterable<InputStream> scan(String name);
}
