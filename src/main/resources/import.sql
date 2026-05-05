ALTER TABLE tlocal_translation
    DROP CONSTRAINT IF EXISTS tlocal_translation_pkey;

ALTER TABLE tlocal_translation
    ADD CONSTRAINT tlocal_translation_pkey
    PRIMARY KEY (language_code, domain, entity, entity_id);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO tresource (restricted,created_at,deleted_at,updated_at,id,resource_id,code,module_code,status,"type",created_by,deleted_by,updated_by,description,endpoint,"name") VALUES
	 (true,'2026-02-05 09:32:42.469',NULL,NULL,'a9a8bca1-4a11-44ea-91a5-6ec4c7c4fc53'::uuid,'ffcc8009-4a0f-4e30-8a2f-8dd6f064d1ed'::uuid,'WAH_MOV','WAH','ACTIVE','menu','system',NULL,NULL,'Warehouse movements','','Movements'),
	 (true,'2026-02-05 09:33:03.226',NULL,NULL,'cfb86fcd-7d1a-4474-b0fd-eccec69ae575'::uuid,'ffcc8009-4a0f-4e30-8a2f-8dd6f064d1ed'::uuid,'WAH_REP','WAH','ACTIVE','menu','system',NULL,NULL,'Warehouse reports','','Reports'),
	 (true,'2026-02-05 09:20:18.338',NULL,'2026-02-05 09:25:09.977','ffcc8009-4a0f-4e30-8a2f-8dd6f064d1ed'::uuid,NULL,'WAH','WAH','ACTIVE','menu','system',NULL,'system','Warehouse menu','','Warehouse'),
	 (true,'2026-02-05 09:27:17.471',NULL,NULL,'26748ab3-aaed-4260-a0ff-b62da8e9b4b8'::uuid,'ffcc8009-4a0f-4e30-8a2f-8dd6f064d1ed'::uuid,'WAH_CONF','WAH','ACTIVE','menu','system',NULL,NULL,'Configuration options for warehouse','','Configuration'),
	 (true,'2026-02-05 09:35:55.606',NULL,NULL,'eb5340ca-37d4-4ce9-9cc0-bc7f49fb3b45'::uuid,'26748ab3-aaed-4260-a0ff-b62da8e9b4b8'::uuid,'WAH_CONF_WAH','WAH','ACTIVE','menu','system',NULL,NULL,'Warehouse management','/warehouse/warehouse','Warehouse'),
	 (true,'2026-02-05 09:40:38.854',NULL,NULL,'6328e0a8-2e01-40a5-8078-b3cc92296c31'::uuid,'a9a8bca1-4a11-44ea-91a5-6ec4c7c4fc53'::uuid,'WAH_MOV_REC','WAH','ACTIVE','view','system',NULL,NULL,'Warehouse Goods receipt','/warehouse/movement/in','Goods receipt'),
	 (true,'2026-02-05 09:41:04.428',NULL,NULL,'4c6d9bb2-2f86-4ca7-8a94-ad0700b84a9a'::uuid,'a9a8bca1-4a11-44ea-91a5-6ec4c7c4fc53'::uuid,'WAH_MOV_ISS','WAH','ACTIVE','view','system',NULL,NULL,'Warehouse Goods issue','/warehouse/movement/out','Goods issue'),
	 (true,'2026-02-05 09:42:42.173',NULL,NULL,'f5504425-e972-465d-94ea-7150ebc98b41'::uuid,'a9a8bca1-4a11-44ea-91a5-6ec4c7c4fc53'::uuid,'WAH_MOV_INV','WAH','ACTIVE','view','system',NULL,NULL,'Warehouse Inventory','/warehouse/inventory','Inventory'),
	 (true,'2026-02-05 09:44:39.237',NULL,NULL,'581baa05-206c-42d7-829e-d063210fd6f3'::uuid,'cfb86fcd-7d1a-4474-b0fd-eccec69ae575'::uuid,'WAH_REP_DAY','WAH','ACTIVE','view','system',NULL,NULL,'Daily movement','/warehouse/rep_daily','Daily movement'),
	 (true,'2026-02-05 09:45:52.887',NULL,NULL,'ca083a13-af05-4c63-b0a3-5dd1afd14c0f'::uuid,'cfb86fcd-7d1a-4474-b0fd-eccec69ae575'::uuid,'WAH_REP_KAR','WAH','ACTIVE','view','system',NULL,NULL,'Material kardex','/warehouse/rep_kardex','Kardex'),
	 (true,'2026-02-05 09:46:49.773',NULL,NULL,'22fdecde-84a8-460a-afb1-8c95d7389e35'::uuid,'cfb86fcd-7d1a-4474-b0fd-eccec69ae575'::uuid,'WAH_REP_STO','WAH','ACTIVE','view','system',NULL,NULL,'Warehouse Stock levels','/warehouse/rep_stock','Stock levels');

INSERT INTO tresource (restricted,created_at,deleted_at,updated_at,id,resource_id,code,module_code,status,"type",created_by,deleted_by,updated_by,description,endpoint,"name") VALUES
	 (true,'2026-03-20 06:03:34.868',NULL,NULL,'19c9cb14-984d-4c26-8639-7e4149fc7750'::uuid,NULL,'CRM','CRM','ACTIVE','menu','system',NULL,NULL,'CRM Menu options','/crm','CRM'),
	 (true,'2026-03-20 06:06:25.846',NULL,NULL,'e5014912-1b7f-49e6-a45f-694d723d2190'::uuid,'19c9cb14-984d-4c26-8639-7e4149fc7750'::uuid,'CRM_CUS','CRM','ACTIVE','view','system',NULL,NULL,'Customers','/crm/person','Customers'),
	 (true,'2026-03-20 06:10:29.562',NULL,NULL,'e6ea9b99-9b63-4115-b586-9b576298163e'::uuid,'19c9cb14-984d-4c26-8639-7e4149fc7750'::uuid,'CRM_CAM','CRM','ACTIVE','menu','system',NULL,NULL,'Campaigns definition','/crm/commercial/campaign','Campaigns'),
	 (true,'2026-03-20 06:12:30.364',NULL,NULL,'7f040433-f33e-4fad-a850-78ac04902849'::uuid,'19c9cb14-984d-4c26-8639-7e4149fc7750'::uuid,'CRM_SAL','CRM','ACTIVE','view','system',NULL,NULL,'Sales','/crm/sales','Sales'),
	 (true,'2026-03-20 06:12:54.348',NULL,NULL,'92644f01-505d-4d3a-b31a-17d920fdcce3'::uuid,'19c9cb14-984d-4c26-8639-7e4149fc7750'::uuid,'CRM_PSA','CRM','ACTIVE','view','system',NULL,NULL,'Post-Sales','/crm/postsales','Post-Sales'),
	 (true,'2026-03-20 06:13:57.386',NULL,NULL,'11617077-c6f5-41d5-b64f-6692b697a448'::uuid,'e6ea9b99-9b63-4115-b586-9b576298163e'::uuid,'CRM_CAM_GEN','CRM','ACTIVE','view','system',NULL,NULL,'General campaign','/crm/commercial/campaign/general','General campaign'),
	 (true,'2026-03-20 06:14:16.434',NULL,NULL,'ca69f358-a52a-4592-b65b-3ceb0938408b'::uuid,'e6ea9b99-9b63-4115-b586-9b576298163e'::uuid,'CRM_CAM_CUS','CRM','ACTIVE','view','system',NULL,NULL,'Custom campaign','/crm/commercial/campaign/custom','Custom campaign'),
	 (true, '2026-04-02 06:12:49.244', NULL, NULL, 'defbef22-0b31-41d4-90a3-dfa7e31b9f23'::uuid, NULL, 'OBI', 'OBI', 'ACTIVE', 'menu', 'system', NULL, NULL, 'Obituary', '', 'Obituary'),
	 (true, '2026-04-02 06:13:34.074', NULL, NULL, '90e91cdf-1904-4c70-a99b-60b2b27ebc0f'::uuid, 'defbef22-0b31-41d4-90a3-dfa7e31b9f23'::uuid, 'OBI-OB', 'OBI', 'ACTIVE', 'view', 'system', NULL, NULL, 'Obituary', '/obituary/obituary', 'Obituary');

INSERT INTO tprofile (created_at,deleted_at,updated_at,id,code,module_code,status,vendor_code,created_by,deleted_by,updated_by,"name") VALUES
	 ('2026-02-06 19:51:17.188831',NULL,NULL,'6be11d65-f359-4573-b4fc-c6b2d4186773'::uuid,'PROF_WAREHOUSE_ADMIN','WAH','ACTIVE','','system',NULL,NULL,'Warehouse Administrator'),
	 ('2026-04-20 14:54:19.560', NULL, NULL, '751a5da0-c5af-4947-8044-2bf9d89fe5ec'::uuid, 'PROF_CRM_ADMIN', 'CRM', 'ACTIVE', '', 'rodrychm@gmail.com', NULL, NULL, 'Administrator profile for CRM'),
	 ('2026-04-20 15:06:01.413', NULL, NULL, '8b9885cc-de7f-4f2d-973f-b540ca1f03af'::uuid, 'PROF_SUPER_ADMIN', 'ALL', 'ACTIVE', '', 'rodrychm@gmail.com', NULL, NULL, 'Super Administrator profile for all modules');
INSERT INTO trole VALUES
	 ('2026-02-06 18:02:35.610682',NULL,NULL,'3a8732ee-baaf-423b-bbcf-59b8c63e3cdb','ROLE_WAREHOUSE_ADMIN','WAH','ACTIVE','','system',NULL,NULL,'Warehouse Administrator'),
	 ('2026-04-20 14:51:28.896', NULL, NULL, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ROLE_CRM_ADMIN', 'CRM', 'ACTIVE', '', 'rodrychm@gmail.com', NULL, NULL, 'Administrator Role for CRM module');

INSERT INTO public.trole_resource
(created_at, deleted_at, updated_at, id, resource_id, role_id, status, created_by, deleted_by, updated_by)
VALUES
('2026-04-20 14:57:52.928', NULL, NULL, '82fa8bcc-9574-4310-9b4f-fbe4d7e3ed9d'::uuid, 'ca083a13-af05-4c63-b0a3-5dd1afd14c0f'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '495ffcd3-de9c-410a-9edf-d44665e494ff'::uuid, 'eb5340ca-37d4-4ce9-9cc0-bc7f49fb3b45'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '1a392247-3b08-48b5-ba7d-0420119a8253'::uuid, '581baa05-206c-42d7-829e-d063210fd6f3'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '831820b2-c607-4c42-bedd-ca72e17ed892'::uuid, '22fdecde-84a8-460a-afb1-8c95d7389e35'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '8631519e-0042-4135-a82a-5b9836512f1e'::uuid, 'a9a8bca1-4a11-44ea-91a5-6ec4c7c4fc53'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '2f471e57-21ef-4eea-a9e8-34c56f9e2602'::uuid, 'f5504425-e972-465d-94ea-7150ebc98b41'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '9530f064-e5c8-4f0b-b9ea-7cfba846bf60'::uuid, '26748ab3-aaed-4260-a0ff-b62da8e9b4b8'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '5914fcac-d5a1-43e2-92af-fdee3493c5d2'::uuid, '6328e0a8-2e01-40a5-8078-b3cc92296c31'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, 'b8779b8a-65f2-4597-805f-747b0b03a6a8'::uuid, 'cfb86fcd-7d1a-4474-b0fd-eccec69ae575'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '688f2d4a-4e66-4626-b303-443db03015bc'::uuid, 'ffcc8009-4a0f-4e30-8a2f-8dd6f064d1ed'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:57:52.928', NULL, NULL, '7cf64e8d-a4b8-4adc-9dfc-0d432a96be1c'::uuid, '4c6d9bb2-2f86-4ca7-8a94-ad0700b84a9a'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:05.171', NULL, NULL, '4b1ddc03-b579-425b-a0aa-54d088b24d74'::uuid, '92644f01-505d-4d3a-b31a-17d920fdcce3'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:05.171', NULL, NULL, '306f209a-1250-4d30-ba03-589962118891'::uuid, 'e5014912-1b7f-49e6-a45f-694d723d2190'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:05.171', NULL, NULL, '38dc37db-a555-41f5-82e0-9d43326a6906'::uuid, '7f040433-f33e-4fad-a850-78ac04902849'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:05.171', NULL, NULL, '4da37ad5-de45-4e1f-949c-c7cb7aca5eb0'::uuid, 'e6ea9b99-9b63-4115-b586-9b576298163e'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:05.171', NULL, NULL, '4265fb86-c208-426b-8476-bf406180ded3'::uuid, '19c9cb14-984d-4c26-8639-7e4149fc7750'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:05.171', NULL, NULL, '0af2d6ba-5175-4b97-8c1a-9dad8011330b'::uuid, 'ca69f358-a52a-4592-b65b-3ceb0938408b'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:05.171', NULL, NULL, 'edae2f96-cb9a-43d2-b234-ab0d820e5503'::uuid, '11617077-c6f5-41d5-b64f-6692b697a448'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:14.693', NULL, NULL, '716bcdef-7f4a-44c1-99fa-1188ff09c76b'::uuid, 'defbef22-0b31-41d4-90a3-dfa7e31b9f23'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:14.693', NULL, NULL, '2a6412c8-96a8-48db-9db6-c08fae46a2b4'::uuid, '90e91cdf-1904-4c70-a99b-60b2b27ebc0f'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:55.653', NULL, NULL, '0c1b29c9-17d6-44c6-a86d-ef53ef90da20'::uuid, '92644f01-505d-4d3a-b31a-17d920fdcce3'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:55.653', NULL, NULL, '210ebf41-2c2c-4395-8f27-7bbfbdaa77d4'::uuid, 'e5014912-1b7f-49e6-a45f-694d723d2190'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:55.653', NULL, NULL, '12f82777-e3fd-4b3d-80ac-3880c2a0874f'::uuid, '7f040433-f33e-4fad-a850-78ac04902849'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:55.653', NULL, NULL, 'dcd972dc-2ce0-4e83-99d7-ae802e4f8aaf'::uuid, 'e6ea9b99-9b63-4115-b586-9b576298163e'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:55.653', NULL, NULL, 'a896ff02-9968-4db9-aec2-c0c7eb33f4a1'::uuid, '19c9cb14-984d-4c26-8639-7e4149fc7750'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:55.653', NULL, NULL, 'cdc8cf33-e304-4e55-9058-b7aff3f82b5e'::uuid, 'ca69f358-a52a-4592-b65b-3ceb0938408b'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 14:58:55.653', NULL, NULL, '23f66f6d-001e-407e-aeef-54c9224f1f5b'::uuid, '11617077-c6f5-41d5-b64f-6692b697a448'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL);

INSERT INTO public.tprofile_role
(created_at, deleted_at, updated_at, id, profile_id, role_id, status, created_by, deleted_by, updated_by)
VALUES
('2026-04-20 15:08:58.316', NULL, NULL, '2d77fcd2-ee2c-43aa-be5e-bf1c491c1d5b'::uuid, '8b9885cc-de7f-4f2d-973f-b540ca1f03af'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 15:09:10.934', NULL, NULL, '19038c02-abd9-4cf0-9f57-6a807d497e54'::uuid, '8b9885cc-de7f-4f2d-973f-b540ca1f03af'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 15:09:57.710', NULL, NULL, '5bd3854c-dd17-4bcd-a866-48d65e70b19d'::uuid, '6be11d65-f359-4573-b4fc-c6b2d4186773'::uuid, '3a8732ee-baaf-423b-bbcf-59b8c63e3cdb'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL),
('2026-04-20 15:10:22.703', NULL, NULL, 'c36a95ad-36c7-436a-abbe-c03ede7597b6'::uuid, '751a5da0-c5af-4947-8044-2bf9d89fe5ec'::uuid, '132b31fe-1996-4b4f-a058-6105f024fed9'::uuid, 'ACTIVE', 'rodrychm@gmail.com', NULL, NULL);