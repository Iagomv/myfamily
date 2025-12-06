# MyFamily Deployment Checklist

## Pre-Deployment (Local Testing)

### Environment Setup

- [ ] Copy `.env.example` to `.env`
- [ ] Generate JWT_SECRET using `./generate-secrets.ps1`
- [ ] Set strong DATABASE_PASSWORD
- [ ] Verify `.env` is in `.gitignore`
- [ ] Review environment variables match your setup

### Local Testing with Docker

- [ ] Docker Desktop installed and running
- [ ] Run: `docker-compose up --build`
- [ ] Wait for all services to start (~30-60 seconds)
- [ ] Frontend loads at: http://localhost:3000
- [ ] API available at: http://localhost:8080/api
- [ ] Swagger UI accessible: http://localhost:8080/api/swagger-ui.html
- [ ] Database connection successful
- [ ] Test login/registration flow
- [ ] Test main features of application
- [ ] Check logs for any errors: `docker-compose logs`
- [ ] Verify no security warnings

### Code Preparation

- [ ] All code committed to Git
- [ ] No uncommitted `.env` files
- [ ] .gitignore includes sensitive files
- [ ] README.md includes deployment link
- [ ] All tests passing locally
- [ ] No hardcoded secrets in code

---

## Railway.app Deployment Checklist

### Account & Project Setup

- [ ] GitHub account created
- [ ] Railway.app account created (sign in with GitHub)
- [ ] Repository pushed to GitHub
- [ ] Railway project created
- [ ] PostgreSQL database provisioned

### Backend Deployment

- [ ] Connect GitHub repository to Railway
- [ ] Set root directory: `backend/myfamily`
- [ ] Generate and set JWT_SECRET
- [ ] Set SPRING_PROFILES_ACTIVE=prod
- [ ] Set DATABASE_URL from PostgreSQL service
- [ ] Set ALLOWED_ORIGINS to frontend URL
- [ ] Backend service deployed and running
- [ ] Test API endpoint
- [ ] Check backend logs for errors

### Frontend Deployment

- [ ] Set root directory: `frontend/myFamily`
- [ ] Set NG_APP_API_URL to backend URL
- [ ] Set NODE_ENV=production
- [ ] Frontend service deployed and running
- [ ] Frontend loads without errors
- [ ] API calls working from frontend
- [ ] Test all major features

### Post-Deployment

- [ ] HTTPS working on both services
- [ ] Database populated correctly
- [ ] Backups configured
- [ ] Monitoring enabled
- [ ] Team notified of deployment URL

---

## Render.com Deployment Checklist

### Account & Services Setup

- [ ] GitHub account created
- [ ] Render.com account created
- [ ] Repository pushed to GitHub
- [ ] PostgreSQL database created on Render
- [ ] Database connection string copied

### Backend Service

- [ ] New Web Service created
- [ ] GitHub repository connected
- [ ] Environment: Docker selected
- [ ] Root directory: `backend/myfamily`
- [ ] Environment variables configured:
  - [ ] SPRING_PROFILES_ACTIVE=prod
  - [ ] DATABASE_URL set correctly
  - [ ] JWT_SECRET generated and set
  - [ ] ALLOWED_ORIGINS set to frontend URL
- [ ] Service deployed successfully
- [ ] Logs reviewed for errors
- [ ] API endpoint tested

### Frontend Service

- [ ] New Web Service created
- [ ] GitHub repository connected
- [ ] Environment: Docker selected
- [ ] Root directory: `frontend/myFamily`
- [ ] Environment variables configured:
  - [ ] NG_APP_API_URL set to backend URL
  - [ ] NODE_ENV=production
- [ ] Service deployed successfully
- [ ] Frontend loads correctly
- [ ] API integration working

### Verification

- [ ] Both services running (green status)
- [ ] No HTTPS warnings
- [ ] All features tested
- [ ] Database accessible
- [ ] Error tracking enabled

---

## Self-Hosted Deployment Checklist

### Server Setup

- [ ] VPS/Cloud server provisioned
- [ ] SSH access configured
- [ ] Operating system: Ubuntu 20.04+ (recommended)
- [ ] Docker installed
- [ ] Docker Compose installed
- [ ] Git installed
- [ ] Firewall configured:
  - [ ] Port 22 (SSH) open
  - [ ] Port 80 (HTTP) open
  - [ ] Port 443 (HTTPS) open
  - [ ] Port 5432 (PostgreSQL) closed to external
  - [ ] Other ports closed

### Repository Setup

- [ ] Repository cloned to server
- [ ] `.env` file created with production values
- [ ] File permissions correct
- [ ] `.env` is NOT committed

### Application Deployment

- [ ] Run: `docker-compose -f docker-compose.prod.yml up -d`
- [ ] Check all containers running: `docker-compose ps`
- [ ] Backend accessible at http://localhost:8080/api
- [ ] Frontend accessible at http://localhost:3000
- [ ] Logs checked for errors

### Nginx Reverse Proxy

- [ ] Nginx installed: `sudo apt install nginx`
- [ ] Config created in `/etc/nginx/sites-available/`
- [ ] Config linked to sites-enabled: `sudo ln -s`
- [ ] Config tested: `sudo nginx -t`
- [ ] Nginx restarted: `sudo systemctl restart nginx`
- [ ] Domain accessible via http://your-domain.com

### HTTPS/SSL Setup

- [ ] Certbot installed
- [ ] SSL certificates generated: `sudo certbot --nginx -d your-domain.com`
- [ ] HTTPS working on domain
- [ ] Auto-renewal configured
- [ ] HTTP redirects to HTTPS

### Database Setup

- [ ] PostgreSQL running in container
- [ ] Data directory backed up
- [ ] Backup script created
- [ ] Cron job for automated backups configured
- [ ] Database connection tested from host

### Monitoring & Logging

- [ ] Log rotation configured
- [ ] Monitoring tool installed (optional)
- [ ] Alert system configured
- [ ] Uptime monitoring enabled
- [ ] Error tracking configured

### Maintenance

- [ ] Documentation of deployment created
- [ ] Admin access secured
- [ ] SSH keys configured (no password auth)
- [ ] Regular backup testing scheduled
- [ ] Update plan established

---

## Post-Deployment (All Platforms)

### Testing

- [ ] Test all user flows:
  - [ ] Registration
  - [ ] Login
  - [ ] Profile management
  - [ ] Family creation
  - [ ] Family member management
  - [ ] Dashboard
  - [ ] Calendar
  - [ ] Shopping list
  - [ ] Documents
- [ ] Test on multiple browsers
- [ ] Test on mobile devices
- [ ] Performance acceptable
- [ ] No console errors

### Security

- [ ] HTTPS working everywhere
- [ ] CORS properly configured
- [ ] JWT tokens working
- [ ] Passwords hashed
- [ ] No sensitive data in logs
- [ ] Security headers present
- [ ] Rate limiting considered

### Monitoring

- [ ] Error tracking set up
- [ ] Performance metrics visible
- [ ] Database health monitored
- [ ] Disk space monitored
- [ ] Alerts configured
- [ ] Uptime monitoring active

### Documentation

- [ ] Deployment process documented
- [ ] Environment variables documented
- [ ] Troubleshooting guide created
- [ ] Admin procedures documented
- [ ] Backup/restore procedures documented
- [ ] Team trained on deployment process

### Communication

- [ ] Team notified of deployment
- [ ] Users notified (if applicable)
- [ ] Access credentials shared securely
- [ ] Support procedure established
- [ ] Issue reporting process documented

---

## Troubleshooting During Deployment

### Docker Issues

- [ ] Docker daemon running
- [ ] No port conflicts
- [ ] Sufficient disk space
- [ ] Network connectivity checked
- [ ] Images downloaded successfully

### Database Issues

- [ ] PostgreSQL initialized
- [ ] Connection string correct
- [ ] Credentials verified
- [ ] Port not firewalled
- [ ] Storage sufficient

### Application Issues

- [ ] Environment variables set
- [ ] Configuration files present
- [ ] Dependencies installed
- [ ] Ports accessible
- [ ] Logs reviewed

### Network Issues

- [ ] DNS resolution working
- [ ] HTTPS certificates valid
- [ ] Firewall rules correct
- [ ] Proxy configuration verified
- [ ] Network latency acceptable

---

## Ongoing Maintenance Checklist (Monthly)

- [ ] Review application logs
- [ ] Check disk space usage
- [ ] Verify backups completed
- [ ] Test restore from backup
- [ ] Update dependencies
- [ ] Monitor performance metrics
- [ ] Review security alerts
- [ ] Check for unused resources
- [ ] Update documentation
- [ ] Communication with team

---

## Emergency Procedures

### Service Down

- [ ] Check Docker containers: `docker-compose ps`
- [ ] Review logs: `docker-compose logs`
- [ ] Restart service: `docker-compose restart`
- [ ] Check database connection
- [ ] Verify environment variables
- [ ] If still down, restore from backup

### Database Corruption

- [ ] Stop application
- [ ] Restore from latest backup
- [ ] Run migrations
- [ ] Verify data integrity
- [ ] Restart application

### Security Breach

- [ ] Rotate JWT_SECRET
- [ ] Rotate database password
- [ ] Review access logs
- [ ] Update firewall rules
- [ ] Notify users if necessary

---

**Document Status:** Complete ✅
**Last Updated:** November 30, 2025
**Version:** 1.0
