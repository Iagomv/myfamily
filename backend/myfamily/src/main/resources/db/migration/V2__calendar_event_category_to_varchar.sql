-- Flyway migration: convert event_category to varchar to allow new values
ALTER TABLE public.calendar_events ALTER COLUMN event_category DROP DEFAULT;
ALTER TABLE public.calendar_events ALTER COLUMN event_category TYPE varchar(255) USING event_category::text;
ALTER TABLE public.calendar_events ALTER COLUMN event_category SET STORAGE EXTENDED;
